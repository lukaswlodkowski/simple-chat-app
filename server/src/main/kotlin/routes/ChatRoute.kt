package org.example.routes

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import io.ktor.websocket.*
import org.example.chat.command.CommandProcessor
import org.example.chat.history.HistoryService
import org.example.chat.session.SessionManager
import java.util.concurrent.atomic.AtomicInteger

fun Routing.chatRoute(
    historyService: HistoryService, commandProcessor: CommandProcessor, sessionManager: SessionManager
) {
    val logger = KtorSimpleLogger(this::class.java.simpleName)
    authenticate("auth-jwt") {
        route("/chat") {
            val userCounter = AtomicInteger(0)
            webSocket {
                userCounter.getAndIncrement()
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                logger.info(username)
                logger.info("Adding user!")
                sessionManager.addSession(username, this)
                try {
                    send("You are connected! There are $userCounter users here.")
                    val historyLog = historyService.getHistoryLog(5)
                    historyLog.reversed().forEach { send("[${it.userId}]: ${it.message}") }
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        historyService.createHistoryLog(username, receivedText) //TODO: Exclude commands from history
                        if (receivedText.startsWith("/")) {
                            commandProcessor.executeCommand(receivedText, username)
                        } else {
                            val textWithUsername = "[${username}]: $receivedText"
                            sessionManager.sessions.entries.forEach {
                                it.value.send(textWithUsername)
                            }
                        }
                    }
                } catch (e: Exception) {
                    logger.error(e.localizedMessage)
                } finally {
                    logger.info("Removing $this!")
                    sessionManager.removeSession(username)
                    userCounter.decrementAndGet()
                }
            }
        }
    }
}