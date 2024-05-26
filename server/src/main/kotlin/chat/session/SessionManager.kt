package org.example.chat.session

import io.ktor.util.logging.*
import io.ktor.websocket.*
import org.example.chat.ChatRoomSession
import org.example.chat.user.User
import java.util.concurrent.ConcurrentHashMap

class SessionManager {
    private val logger = KtorSimpleLogger(this::class.java.simpleName)

    private val chatSessions = ConcurrentHashMap<String, ConcurrentHashMap<String, ChatRoomSession>>()

    fun addSession(chatId: String, user: User, session: WebSocketSession) {
        if (chatSessions.containsKey(chatId)) {
            val specificChatSessions = chatSessions[chatId] ?: mutableMapOf()
            if (!specificChatSessions.containsKey(user.name)) {
                specificChatSessions[user.name] = ChatRoomSession(user, session)
            }
        } else {
            chatSessions[chatId] =
                ConcurrentHashMap(mutableMapOf(user.name to ChatRoomSession(user, session)))
        }
    }

    fun getSessionsForChat(chatId: String) = chatSessions[chatId] ?: emptyMap()
    fun getSessionsForChatAndUser(chatId: String, username: String) =
        getSessionsForChat(chatId)[username]

    suspend fun removeSession(chatId: String, username: String) {
        chatSessions[chatId]?.get(username)?.session?.close(CloseReason(CloseReason.Codes.NORMAL, "Exited"))
        chatSessions[chatId]?.remove(username)
        logger.info("Removing session for chat: $chatId user: $username")
    }
}