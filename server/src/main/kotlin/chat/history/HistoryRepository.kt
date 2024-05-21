package org.example.chat.history

import org.example.model.ConversationHistoryEntity
import org.example.model.ConversationHistoryTable
import org.example.model.ConversationHistoryTable.chatId
import org.example.model.ConversationHistoryTable.id
import org.example.model.ConversationHistoryTable.message
import org.example.model.ConversationHistoryTable.messageTime
import org.example.model.ConversationHistoryTable.userId
import org.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import java.time.LocalDateTime
import java.util.*

class HistoryRepository {
    suspend fun createHistoryLog(userMessage: String): ConversationHistoryEntity? = dbQuery {
        val insertStmt = ConversationHistoryTable.insert {
            it[id] = UUID.randomUUID()
            it[chatId] = UUID.randomUUID()
            it[userId] = UUID.randomUUID()
            it[message] = userMessage
            it[messageTime] = LocalDateTime.now()
        }
        insertStmt.resultedValues?.singleOrNull()?.let { resultRowToHistory(it) }
    }

    private fun resultRowToHistory(resultRow: ResultRow): ConversationHistoryEntity =
        ConversationHistoryEntity(
            id = resultRow[id],
            chatId = resultRow[chatId],
            userId = resultRow[userId],
            message = resultRow[message],
            messageTime = resultRow[messageTime],
        )

}