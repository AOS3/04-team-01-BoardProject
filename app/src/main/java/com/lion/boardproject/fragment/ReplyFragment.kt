package com.lion.boardproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.boardproject.BoardActivity
import com.lion.boardproject.R
import com.lion.boardproject.databinding.FragmentReplyBinding
import com.lion.boardproject.databinding.RowReplyListBinding
import com.lion.boardproject.model.ReplyModel
import com.lion.boardproject.service.ReplyService
import com.lion.boardproject.util.ReplyState
import com.lion.boardproject.viewmodel.ReplyViewModel
import com.lion.boardproject.viewmodel.RowReplyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class ReplyFragment : BottomSheetDialogFragment() {

    lateinit var fragmentReplyBinding: FragmentReplyBinding
    lateinit var boardActivity: BoardActivity
    private lateinit var boardDocumentId: String

    private val replyService = ReplyService()

    var recyclerViewList = mutableListOf<ReplyModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            boardDocumentId = it.getString("boardDocumentId") ?: ""
        }

        if (boardDocumentId.isEmpty()) {
            throw IllegalArgumentException("boardDocumentId가 전달되지 않았습니다.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReplyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reply, container, false)
        fragmentReplyBinding.replyViewModel = ReplyViewModel(this@ReplyFragment)
        fragmentReplyBinding.lifecycleOwner = this@ReplyFragment

        boardActivity = activity as BoardActivity

        // RecyclerView 구성을 위한 리스트를 초기화한다.
        recyclerViewList.clear()
        // arguments의 값을 변수에 담아주는 메서드를 호출한다.
        gettingArguments()
        // RecyclerView 구성 메서드
        settingCommentRecyclerView()
        refreshMainRecyclerView()

        return fragmentReplyBinding.root
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments(){
        boardDocumentId = arguments?.getString("boardDocumentId")!!
    }

    // 댓글 작성 완료 메서드
    fun proReplyWriteSubmit() {
        fragmentReplyBinding.apply {
            // 댓글 작성자 닉네임
            val replyNickName = boardActivity.loginUserNickName
            // 댓글 내용
            val replyText = replyViewModel?.textFieldCommentText?.value.orEmpty()
            // 작성 시간
            val replyTimeStamp = System.currentTimeMillis()
            // 상태
            val replyState = ReplyState.REPLY_STATE_NORMAL

            // 입력값 검증
            if (replyText.isBlank()) {
                boardActivity.showMessageDialog("입력 오류", "댓글 내용을 입력해주세요", "확인") {
                    boardActivity.showSoftInput(textFieldReply.editText!!)
                }
                return
            }

            // 댓글 모델 생성
            val replyModel = ReplyModel().apply {
                this.replyNickName = replyNickName
                this.replyText = replyText
                this.replyBoardId = boardDocumentId
                this.replyTimeStamp = replyTimeStamp
                this.replyState = replyState
            }

            Log.d("ReplyFragment", "닉네임: $replyNickName, 댓글내용: $replyText, 작성시간: $replyTimeStamp")

            // Firestore에 댓글 저장
            CoroutineScope(Dispatchers.Main).launch {
                val success = replyService.addReply(boardDocumentId, replyModel)
                if(success) {
                    // 댓글 목록 새로고침
                    refreshMainRecyclerView()
                    // 입력 필드 초기화
                    textFieldReply.editText?.setText("")
                    Toast.makeText(context, "댓글 추가 완료!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "댓글 추가 실패...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 데이터를 가져와 MainRecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            val replies = withContext(Dispatchers.IO) {
                replyService.getReplies(boardDocumentId)
            }

            // 상태가 NORMAL(1)인 댓글만 리스트에 추가
            recyclerViewList.clear()
            recyclerViewList.addAll(replies.filter { it.replyState == ReplyState.REPLY_STATE_NORMAL })

            fragmentReplyBinding.recyclerViewReply.adapter?.notifyDataSetChanged()
        }
    }



    // Timestamp를 String으로 변환하는 함수
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(timestamp)
    }

    fun onReplyDeleteRequested(rowReplyViewModel: RowReplyViewModel) {
        val position = recyclerViewList.indexOfFirst {
            it.replyNickName == rowReplyViewModel.textViewRowCommentNickNameText.value &&
                    it.replyText == rowReplyViewModel.textViewRowCommentTextText.value
        }

        if (position != -1) {
            deleteReply(position)
        }
    }

    // 댓글 삭제 메서드
    fun deleteReply(position: Int) {
        val replyToDelete = recyclerViewList[position] // 정확한 position으로 댓글 선택

        // 다이얼로그를 통해 삭제 확인
        boardActivity.showMessageDialog(
            title = "댓글 삭제",
            message = "댓글을 삭제하시겠습니까?",
            posTitle = "확인"
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                replyService.updateReplyState(
                    replyToDelete.replyBoardId,
                    replyToDelete.replyDocumentId,
                    ReplyState.REPLY_STATE_DELETE
                )
                // 삭제된 댓글 RecyclerView 목록에서 제거
                withContext(Dispatchers.Main) {
                    recyclerViewList.removeAt(position)
                    fragmentReplyBinding.recyclerViewReply.adapter?.notifyItemRemoved(position)
                }
            }
        }
    }




    // RecyclerView 구성 메서드
    fun settingCommentRecyclerView(){
        fragmentReplyBinding.apply {
            recyclerViewReply.adapter = ReplyRecyclerViewAdapter()
            recyclerViewReply.layoutManager = LinearLayoutManager(requireContext())
            val deco = MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)
            recyclerViewReply.addItemDecoration(deco)
        }
    }

    // RecyclerView의 어뎁터
    inner class ReplyRecyclerViewAdapter : RecyclerView.Adapter<ReplyRecyclerViewAdapter.ReplyViewHolder>() {
        inner class ReplyViewHolder(val rowReplyListBinding: RowReplyListBinding) :
            RecyclerView.ViewHolder(rowReplyListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
            val rowReplyListBinding = DataBindingUtil.inflate<RowReplyListBinding>(
                layoutInflater,
                R.layout.row_reply_list,
                parent,
                false
            )
            rowReplyListBinding.rowReplyViewModel =
                RowReplyViewModel(this@ReplyFragment)
            rowReplyListBinding.lifecycleOwner = this@ReplyFragment

            val replyViewHolder = ReplyViewHolder(rowReplyListBinding)
            return replyViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size
        }

        override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
            val reply = recyclerViewList[position]
            holder.rowReplyListBinding.rowReplyViewModel?.apply {
                textViewRowCommentNickNameText.value = reply.replyNickName
                textViewRowCommentTextText.value = reply.replyText
                textViewRowCommentDateText.value = formatTimestamp(reply.replyTimeStamp)
            }
            // 삭제 버튼 가시성 설정
            val isDeletable = reply.replyNickName == boardActivity.loginUserNickName // 로그인 사용자와 댓글 작성자 비교
            holder.rowReplyListBinding.buttonReplyDelete.visibility =
                if (isDeletable) View.VISIBLE else View.GONE

        }
    }
}