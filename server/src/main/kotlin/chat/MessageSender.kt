package org.example.chat

import io.ktor.websocket.*
import org.example.chat.session.SessionManager

class MessageSender(private val sessionManager: SessionManager) {
    suspend fun send(chatId: String, username: String, message: String) {
        sessionManager.getSessionsForChatAndUser(chatId, username)?.session?.send(message)
    }

    suspend fun sendToAll(chatId: String, username: String, message: String) {
        sessionManager.getSessionsForChat(chatId).forEach {
            it.value.session.send("[${username}]: $message")
        }
    }
}