package org.example.chat

import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import io.ktor.websocket.*
import org.example.chat.command.CommandProcessor
import org.example.chat.history.HistoryService
import org.example.chat.user.User
import org.example.chat.user.UserManager

data class ChatRoomSession(val user: User, val session: WebSocketSession)

class ChatHandler(
    private val historyService: HistoryService,
    private val commandProcessor: CommandProcessor,
    private val userManager: UserManager,
    private val messageSender: MessageSender
) {
    private val logger = KtorSimpleLogger(this::class.java.simpleName)
    suspend fun handle(session: DefaultWebSocketServerSession, chatId: String) {
        val user = userManager.userJoinedChat(chatId, session)
        try {
            initialMessages(chatId, user.name)
            for (frame in session.incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                if (receivedText.startsWith("/")) {
                    commandProcessor.executeCommand("$chatId-${user.name}-$receivedText")
                } else {
                    historyService.createHistoryLog(user.name, receivedText) //TODO: Fix history per chat
                    messageSender.sendToAll(chatId, user.name, receivedText)
                }
            }
        } catch (e: Exception) {
            logger.error(e.localizedMessage)
        } finally {
            userManager.userLeftChat(chatId, session)
        }
    }

    private suspend fun initialMessages(chatId: String, username: String) {
        messageSender.send(
            chatId,
            username,
            "You are connected! There are ${userManager.getNumberOfUsersInChat(chatId)} users here."
        )
        val historyLog = historyService.getHistoryLog(5)
        historyLog.reversed().forEach { messageSender.send(chatId, username, "[${it.userId}]: ${it.message}") }
    }
}