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
        val username = userManager.userJoinedChat(session)
        try {
            initialMessages(username)
            for (frame in session.incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                historyService.createHistoryLog(username, receivedText) //TODO: Exclude commands from history
                if (receivedText.startsWith("/")) {
                    commandProcessor.executeCommand(receivedText, username)
                } else {
                    messageSender.sendToAll(username, receivedText)
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