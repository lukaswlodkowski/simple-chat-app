package org.example.routes

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.example.chat.ChatHandler
import org.example.plugins.authName

fun Routing.chatRoute(
    chatHandler: ChatHandler
) {
    authenticate(authName) {
        route("/chat/{id}") {
            webSocket {
                val chatId = call.parameters["id"] ?: throw Exception("Chat id was not passed as a parameter.")
                chatHandler.handle(this, chatId)
            }
        }
    }
}