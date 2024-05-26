package org.example.chat.command

import org.example.chat.session.SessionManager

class KickCommand(private val sessionManager: SessionManager) {
    suspend fun executeCommand(chatId: String, commandArgs: String) {
        sessionManager.removeSession(chatId, commandArgs)
    }
}