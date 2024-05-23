package org.example.chat.command

import io.ktor.websocket.*
import org.example.chat.SessionManager

class ExitCommand(private val sessionManager: SessionManager) {
    suspend fun executeCommand(session: WebSocketSession) = sessionManager.removeSession(session)
}