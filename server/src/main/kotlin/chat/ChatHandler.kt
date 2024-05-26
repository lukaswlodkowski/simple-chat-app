package org.example.chat

import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import io.ktor.websocket.*
import org.example.chat.command.CommandProcessor
import org.example.chat.history.HistoryService
import org.example.chat.user.UserManager

class ChatHandler(
    private val historyService: HistoryService,
    private val commandProcessor: CommandProcessor,
    private val userManager: UserManager,
    private val messageSender: MessageSender
) {
    private val logger = KtorSimpleLogger(this::class.java.simpleName)

    suspend fun handle(session: DefaultWebSocketServerSession) {
        val user = userManager.userJoinedChat(session)
        try {
            initialMessages(user.name)
            for (frame in session.incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                if (receivedText.startsWith("/")) {
                    commandProcessor.executeCommand(receivedText, user.name)
                } else {
                    historyService.createHistoryLog(user.name, receivedText)
                    messageSender.sendToAll(user.name, receivedText)
                }
            }
        } catch (e: Exception) {
            logger.error(e.localizedMessage)
        } finally {
            userManager.userLeftChat(session)
        }
    }

    private suspend fun initialMessages(username: String) {
        messageSender.send(
            username,
            "You are connected! There are ${userManager.getNumberOfUsersInChat()} users here."
        )
        val historyLog = historyService.getHistoryLog(5)
        historyLog.reversed().forEach { messageSender.send(username, "[${it.userId}]: ${it.message}") }
    }
}