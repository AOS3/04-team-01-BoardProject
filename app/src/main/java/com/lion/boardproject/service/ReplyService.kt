package com.lion.boardproject.service

import android.util.Log
import com.lion.boardproject.model.ReplyModel
import com.lion.boardproject.repository.ReplyRepository
import com.lion.boardproject.util.ReplyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReplyService {

    private val repository = ReplyRepository()

    // 댓글 추가
    suspend fun addReply(boardId: String, reply: ReplyModel): Boolean = withContext(Dispatchers.IO) {
        try {
            repository.addReply(boardId, reply)
            true // 성공 시 true 반환
        } catch (e: Exception) {
            e.printStackTrace()
            false // 실패 시 false 반환
        }
    }

    // 댓글 목록을 가져오는 메서드
    suspend fun getReplies(boardId: String): List<ReplyModel> = withContext(Dispatchers.IO) {
        repository.getReplies(boardId)
    }

    suspend fun updateReplyState(boardId: String, replyId: String, newState: ReplyState): Boolean {
        return try {
            repository.updateReplyState(boardId, replyId, newState)
            true
        } catch (e: Exception) {
            false
        }
    }

}
