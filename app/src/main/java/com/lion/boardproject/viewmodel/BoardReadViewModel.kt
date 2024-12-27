package com.lion.boardproject.viewmodel

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.lion.boardproject.fragment.BoardReadFragment
import com.lion.boardproject.fragment.BoardWriteFragment

class BoardReadViewModel(val boardReadFragment: BoardReadFragment) : ViewModel() {
    // textFieldBoardReadTitle - text
    val textFieldBoardReadTitleText = MutableLiveData(" ")
    // textFieldBoardReadNickName - text
    val textFieldBoardReadNickName = MutableLiveData(" ")
    // textFieldBoardReadType - text
    val textFieldBoardReadTypeText = MutableLiveData(" ")
    // textFieldBoardReadText - text
    val textFieldBoardReadTextText = MutableLiveData(" ")

    // fabBoardRead - onClick
    var boardWriteId: String = "" // 게시글 ID 저장
    fun fabBoardReadOnClick(){
        boardReadFragment.openBottomSheet()
    }
// 뷰모델의 boardWriteId를 초기화
//    var boardWriteId: String = "" // 게시글 ID 저장
//
//    fun fabBoardReadOnClick() {
//        if (boardWriteId.isNotEmpty()) { // boardWriteId가 설정되어 있는지 확인
//            boardReadFragment.openBottomSheet(boardWriteId)
//        } else {
//            Log.e("ViewModel", "boardWriteId가 설정되지 않았습니다.")
//        }
//    }

    companion object{
        // toolbarBoardRead - onNavigationClickBoardRead
        @JvmStatic
        @BindingAdapter("onNavigationClickBoardRead")
        fun onNavigationClickBoardRead(materialToolbar: MaterialToolbar, boardReadFragment: BoardReadFragment){
            materialToolbar.setNavigationOnClickListener {
                boardReadFragment.movePrevFragment()
            }
        }
    }
}