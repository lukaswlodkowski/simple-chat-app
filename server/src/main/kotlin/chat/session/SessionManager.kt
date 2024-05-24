package org.example.chat.session

import io.ktor.util.logging.*
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class SessionManager {
    private val logger = KtorSimpleLogger(this::class.java.simpleName)

    val sessions = ConcurrentHashMap<String, WebSocketSession>()

    fun addSession(username: String, session: WebSocketSession) {
        sessions[username] = session
    }

    fun getSessionForUser(username: String) = sessions[username]

    suspend fun removeSession(username: String) {
        val session = sessions[username]
        logger.info("Removing $session!")
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Exited"))
        sessions.remove(username)
    }
}