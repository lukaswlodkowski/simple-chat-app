package org.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.chat.ChatHandler
import org.example.routes.chatRoute
import org.koin.ktor.ext.get

fun Application.configureRouting(
    chatHandler: ChatHandler = get()
) {
    routing {
        chatRoute(chatHandler)
    }
}