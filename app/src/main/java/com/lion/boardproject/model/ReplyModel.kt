package com.lion.boardproject.model

import com.lion.boardproject.util.ReplyState
import com.lion.boardproject.vo.ReplyVO

data class ReplyModel(
    // 댓글 문서 id
    var replyDocumentId:String = "",
    // 댓글 작성자 닉네임
    var replyNickName:String = "",
    // 댓글 내용
    var replyText:String = "",
    // 댓글이 달린 글 구분 값
    var replyBoardId:String = "",
    // 시간
    var replyTimeStamp:Long = 0L,
    // 상태
    var replyState:ReplyState = ReplyState.REPLY_STATE_NORMAL

){

    fun toReplyVO() : ReplyVO{
        val replyVO = ReplyVO()

        replyVO.replyNickName = replyNickName
        replyVO.replyText = replyText
        replyVO.replyBoardId = replyBoardId
        replyVO.replyTimeStamp = replyTimeStamp
        replyVO.replyState = replyState.number

        return replyVO
    }
}