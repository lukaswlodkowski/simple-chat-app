package org.example.chat.command

import io.ktor.util.logging.*

class MessageCommandProcessor(
    private val exitCommand: ExitCommand,
    private val kickCommand: KickCommand
) : CommandProcessor {
    private val logger = KtorSimpleLogger(this::class.java.simpleName)

    override suspend fun executeCommand(commandWithMetadata: String) {
        val parts = commandWithMetadata.split("-")
        val chatId = parts[0].lowercase()
        val username = parts[1].lowercase()
        val command = parts[2].lowercase()
        val commandParts = command.split(" ")
        val commandBase = commandParts[0].lowercase()
        val commandArgs = commandParts[1].lowercase()
        logger.info("User $username is executing command $command")
        when (commandBase) {
            CommandValues.EXIT.commandName -> exitCommand.executeCommand(chatId, username)
            CommandValues.KICK.commandName -> kickCommand.executeCommand(chatId, commandArgs)
            else -> throw Exception("Unknown command: $commandBase")
        }
    }
}

enum class CommandValues(val commandName: String) {
    EXIT("/exit"),
    KICK("/kick")
}