package com.lion.boardproject.viewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lion.boardproject.fragment.BoardReadFragment
import com.lion.boardproject.fragment.ReplyFragment

class ReplyViewModel(val replyFragment: ReplyFragment) : ViewModel() {
    // textFieldComment - text
    val textFieldCommentText = MutableLiveData("")

    // buttonCommentAdd - onClick
    // 댓글 작성 완료
    fun buttonCommentAddOnClick() {
        replyFragment.proReplyWriteSubmit()
    }

    companion object{
        // toolbarBoardRead - onNavigationClickBoardRead
        @JvmStatic
        @BindingAdapter("setEndIconOnClickListener")
        fun setEndIconOnClickListener(textInputLayout: TextInputLayout, replyFragment: ReplyFragment){
            textInputLayout.setEndIconOnClickListener {
                replyFragment.proReplyWriteSubmit()
            }
        }
    }
}