package com.lion.boardproject.vo

import com.lion.boardproject.model.ReplyModel
import com.lion.boardproject.util.ReplyState

data class ReplyVO(
    var replyNickName: String = "",  // 댓글 작성자 닉네임
    var replyText: String = "",      // 댓글 내용
    var replyBoardId: String = "",   // 댓글이 달린 글 구분값
    var replyTimeStamp: Long = 0L,   // 작성 시간
    var replyState: Int = 0          // 댓글 상태 (숫자로 저장)
) {
    fun toReplyModel(replyDocumentId: String): ReplyModel {
        return ReplyModel(
            replyDocumentId = replyDocumentId,
            replyNickName = replyNickName,
            replyText = replyText,
            replyBoardId = replyBoardId,
            replyTimeStamp = replyTimeStamp,
            replyState = when (replyState) {
                ReplyState.REPLY_STATE_NORMAL.number -> ReplyState.REPLY_STATE_NORMAL
                ReplyState.REPLY_STATE_DELETE.number -> ReplyState.REPLY_STATE_DELETE
                else -> ReplyState.REPLY_STATE_NORMAL // 기본 상태
            }
        )
    }
}
