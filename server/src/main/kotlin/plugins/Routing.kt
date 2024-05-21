package org.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.chat.SessionManager
import org.example.chat.command.MessageCommandProcessor
import org.example.chat.history.HistoryService
import org.example.routes.chatRoute
import org.koin.ktor.ext.get

fun Application.configureRouting(
    historyService: HistoryService = get(),
    messageCommandProcessor: MessageCommandProcessor = get(),
    sessionManager: SessionManager = get()
) {
    routing {
        chatRoute(historyService, messageCommandProcessor, sessionManager)
    }
}