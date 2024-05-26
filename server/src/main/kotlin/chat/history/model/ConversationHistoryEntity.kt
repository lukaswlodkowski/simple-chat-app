package org.example.chat.history.model

import kotlinx.serialization.Serializable
import org.example.configuration.LocalDateTimeSerializer
import org.example.configuration.UUIDSerializer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

@Serializable
data class ConversationHistoryEntity(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class)
    val chatId: UUID = UUID.randomUUID(),
    val userId: String,
    val message: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val messageTime: LocalDateTime
)

object ConversationHistoryTable : Table() {
    val id = uuid("id")
    val chatId = uuid("chatId")
    val userId = varchar("userId", 16)
    val message = varchar("message", 256)
    val messageTime = datetime("message_date_time")

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}
