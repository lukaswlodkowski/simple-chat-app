package org.example.chat.command

import io.ktor.util.logging.*

class MessageCommandProcessor(
    private val exitCommand: ExitCommand,
    private val kickCommand: KickCommand
) : CommandProcessor {
    private val logger = KtorSimpleLogger(this::class.java.simpleName)

    override suspend fun executeCommand(command: String, username: String) {
        logger.info("User $username is executing command $command")
        val commandBase = command.split(" ")[0].lowercase()
        when (commandBase) {
            CommandValues.EXIT.commandName -> exitCommand.executeCommand(username)
            CommandValues.KICK.commandName -> kickCommand.executeCommand(command)
            else -> throw Exception("Unknown command")
        }
    }
}

enum class CommandValues(val commandName: String) {
    EXIT("/exit"),
    KICK("/kick")
}