package org.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.history.HistoryService
import org.example.routes.chatRoute
import org.koin.ktor.ext.get

fun Application.configureRouting(
    historyService: HistoryService = get()
) {
    routing {
        chatRoute(historyService)
    }
}