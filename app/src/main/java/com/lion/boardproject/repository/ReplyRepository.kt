package com.lion.boardproject.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.boardproject.model.ReplyModel
import com.lion.boardproject.util.ReplyState
import kotlinx.coroutines.tasks.await

class ReplyRepository {

    private val db = FirebaseFirestore.getInstance()

    // 댓글 추가 메서드
    suspend fun addReply(boardId: String, reply: ReplyModel) {
        val replyDocument = db.collection("BoardData")
            .document(boardId)
            .collection("boardReply")
            .document() // 새로운 댓글 문서 생성

        reply.replyDocumentId = replyDocument.id
        replyDocument.set(reply.toReplyVO()).await() // 댓글 데이터 저장
    }

    suspend fun getReplies(boardId: String): MutableList<ReplyModel> {
        val replyCollection = db.collection("BoardData")
            .document(boardId)
            .collection("boardReply") // 서브 컬렉션 접근
            .orderBy("replyTimeStamp")
        try {
            val documents = replyCollection.get().await()

            return documents.map { document ->
                val replyMap = document.data

                ReplyModel().apply {
                    replyNickName = replyMap["replyNickName"] as String
                    replyText = replyMap["replyText"] as String
                    replyBoardId = boardId
                    replyTimeStamp = replyMap["replyTimeStamp"] as Long
                    replyState = when (val state = replyMap["replyState"]) {
                        is Long -> ReplyState.values().firstOrNull { it.number == state.toInt() } ?: ReplyState.REPLY_STATE_NORMAL
                        is String -> ReplyState.valueOf(state)
                        else -> ReplyState.REPLY_STATE_NORMAL // 기본값
                    }
                    replyDocumentId = document.id
                }
            }.toMutableList()
        } catch (e: Exception) {
            return mutableListOf()
        }
    }

    suspend fun updateReplyState(boardId: String, replyId: String, newState: ReplyState) {
        val replyDocument = db.collection("BoardData")
            .document(boardId)
            .collection("boardReply")
            .document(replyId)

        replyDocument.update("replyState", newState.number).await()
    }

}
