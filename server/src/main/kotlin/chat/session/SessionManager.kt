package org.example.chat.session

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class SessionManager {
    val sessions = ConcurrentHashMap<String, WebSocketSession>()

    fun addSession(username: String, session: WebSocketSession) {
        sessions[username] = session
    }

    suspend fun removeSession(username: String) {
        val session = sessions[username]
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Exited"))
        sessions.remove(username)
    }
}