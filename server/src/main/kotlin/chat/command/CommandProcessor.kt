package org.example.chat.command

interface CommandProcessor {
    suspend fun executeCommand(command: String)
}