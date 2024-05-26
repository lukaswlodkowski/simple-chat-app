package org.example.chat.history

import org.example.chat.history.model.ConversationHistoryEntity
import org.example.chat.history.model.ConversationHistoryTable
import org.example.chat.history.model.ConversationHistoryTable.chatId
import org.example.chat.history.model.ConversationHistoryTable.id
import org.example.chat.history.model.ConversationHistoryTable.message
import org.example.chat.history.model.ConversationHistoryTable.messageTime
import org.example.chat.history.model.ConversationHistoryTable.userId
import org.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDateTime
import java.util.*

class HistoryRepository {
    suspend fun createHistoryLog(username: String, userMessage: String): ConversationHistoryEntity? = dbQuery {
        val insertStmt = ConversationHistoryTable.insert {
            it[id] = UUID.randomUUID()
            it[chatId] = UUID.randomUUID()
            it[userId] = username
            it[message] = userMessage
            it[messageTime] = LocalDateTime.now()
        }
        insertStmt.resultedValues?.singleOrNull()?.let { resultRowToHistory(it) }
    }

    suspend fun getHistoryLogs(numberOfPreviousMessages: Int): List<ConversationHistoryEntity> = dbQuery {
        ConversationHistoryTable.selectAll()
            .orderBy(messageTime, SortOrder.DESC)
            .limit(numberOfPreviousMessages)
            .map { resultRowToHistory(it) }
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