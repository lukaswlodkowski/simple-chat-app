package org.example.chat.command

import org.example.chat.SessionManager

class KickCommand(private val sessionManager: SessionManager) {
    suspend fun executeCommand(command: String) {
        sessionManager.removeSession(command.split(" ")[1].lowercase())
    }
}