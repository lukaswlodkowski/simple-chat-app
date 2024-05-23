package org.example.routes

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import io.ktor.websocket.*
import org.example.Connection
import org.example.chat.SessionManager
import org.example.chat.command.CommandProcessor
import org.example.chat.history.HistoryService
import java.util.*

fun Routing.chatRoute(
    historyService: HistoryService,
    commandProcessor: CommandProcessor,
    sessionManager: SessionManager
) {
    val logger = KtorSimpleLogger(this::class.java.simpleName)
    route("/chat") {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket {
            logger.info("Adding user!")
            val thisConnection = Connection(this)
            sessionManager.addSession(thisConnection.name, this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                val historyLog = historyService.getHistoryLog(5)
                historyLog.reversed().forEach { send("[${it.userId}]: ${it.message}") }
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    historyService.createHistoryLog(thisConnection.name, receivedText)
                    if (receivedText.startsWith("/")) {
                        commandProcessor.executeCommand(receivedText, thisConnection.name)
                    } else {
                        val textWithUsername = "[${thisConnection.name}]: $receivedText"
                        connections.forEach {
                            it.session.send(textWithUsername)
                        }
                    }
                }
            } catch (e: Exception) {
                logger.error(e.localizedMessage)
            } finally {
                logger.info("Removing $thisConnection!")
                connections -= thisConnection
                sessionManager.removeSession(thisConnection.name)
            }
        }
    }
}