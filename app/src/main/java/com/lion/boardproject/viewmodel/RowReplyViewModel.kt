package com.lion.boardproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lion.boardproject.fragment.ReplyFragment

class RowReplyViewModel(
    private val replyFragment: ReplyFragment) {
    var textViewRowCommentNickNameText = MutableLiveData<String>()
    var textViewRowCommentTextText = MutableLiveData<String>()
    var textViewRowCommentDateText = MutableLiveData<String>()

    // 삭제 버튼 클릭 이벤트 처리
    fun buttonReplyDeleteOnClick() {
        replyFragment.onReplyDeleteRequested(this)
    }
}
