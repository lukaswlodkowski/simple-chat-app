package org.example.chat.command

import io.ktor.websocket.*

class MessageCommandProcessor(private val exitCommand: ExitCommand) {
    suspend fun process(message: String, session: WebSocketSession) {
        when (message.lowercase()) {
            Command.EXIT.name -> exitCommand.executeCommand(session)
            else -> throw Exception("Unknown command")
        }
    }

    fun isCommand(message: String) = message.startsWith("/")
}

enum class Command(name: String) {
    EXIT("/exit")
}