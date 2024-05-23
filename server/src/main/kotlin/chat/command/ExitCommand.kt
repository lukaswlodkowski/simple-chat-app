package org.example.chat.command

import org.example.chat.SessionManager

class ExitCommand(private val sessionManager: SessionManager) {
    suspend fun executeCommand(username: String) = sessionManager.removeSession(username)
}