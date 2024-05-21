package org.example.routes

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import io.ktor.websocket.*
import org.example.Connection
import org.example.chat.SessionManager
import org.example.chat.command.MessageCommandProcessor
import org.example.chat.history.HistoryService
import java.util.*

fun Routing.chatRoute(historyService: HistoryService, messageCommandProcessor: MessageCommandProcessor, sessionManager: SessionManager) {
    val logger = KtorSimpleLogger(this::class.java.simpleName)
    route("/chat") {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket {
            println("Adding user!")
            val thisConnection = Connection(this)
            sessionManager.addSession(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    historyService.createHistoryLog(receivedText)
                    if (messageCommandProcessor.isCommand(receivedText)) {
                        messageCommandProcessor.process(receivedText, this)
                    } else {
                        val textWithUsername = "[${thisConnection.name}]: $receivedText"
                        connections.forEach {
                            it.session.send(textWithUsername)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
    }
}