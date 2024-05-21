package org.example.chat

import io.ktor.websocket.*
import java.util.*

class SessionManager {
    companion object {
        val sessions = Collections.synchronizedSet<WebSocketSession>(LinkedHashSet())
    }

    fun addSession(session: WebSocketSession) {
        sessions.add(session)
    }

    suspend fun removeSession(session: WebSocketSession) {
        session.close(CloseReason(CloseReason.Codes.NORMAL, "Exited"))
        sessions.remove(session)
    }
}