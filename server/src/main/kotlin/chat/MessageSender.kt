package org.example.chat

import io.ktor.websocket.*
import org.example.chat.session.SessionManager

class MessageSender(private val sessionManager: SessionManager) {
    suspend fun send(username: String, message: String) {
        sessionManager.getSessionForUser(username)?.send(message)
    }

    suspend fun sendToAll(username: String, message: String) {
        sessionManager.sessions.entries.forEach {
            send(it.key, "[${username}]: $message")
        }

    }
}