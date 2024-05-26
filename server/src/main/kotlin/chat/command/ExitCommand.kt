package org.example.chat.command

import org.example.chat.session.SessionManager

class ExitCommand(private val sessionManager: SessionManager) {
    suspend fun executeCommand(chatId: String, username: String) = sessionManager.removeSession(chatId, username)
}