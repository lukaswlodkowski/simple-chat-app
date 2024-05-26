package org.example.chat.user

import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import org.example.chat.session.SessionManager

class UserManager(
    private val sessionManager: SessionManager,
    private val userResolver: UserResolver
) {
    private val logger = KtorSimpleLogger(this::class.java.simpleName)

    fun userJoinedChat(chatId: String, session: DefaultWebSocketServerSession): User {
        val user = userResolver.resolveUser(session)
        logger.info("User: ${user.name} joined chat!")
        sessionManager.addSession(chatId, user, session)
        return user
    }

    suspend fun userLeftChat(chatId: String, session: DefaultWebSocketServerSession) {
        sessionManager.removeSession(chatId, userResolver.resolveUser(session).name)
    }

    fun getNumberOfUsersInChat(chatId: String) = sessionManager.getSessionsForChat(chatId).size
}