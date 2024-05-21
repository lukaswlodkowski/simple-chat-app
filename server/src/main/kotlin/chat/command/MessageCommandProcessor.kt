package org.example.chat.command

import io.ktor.websocket.*
import org.example.chat.SessionManager

class MessageCommandProcessor(private val sessionManager: SessionManager) {
    suspend fun process(message: String, session: WebSocketSession) {
        if (message.startsWith("/")) {
            if (message == "/exit") {
                sessionManager.removeSession(session)
            }
        }
    }

    fun isCommand(message: String) = message.startsWith("/")
}