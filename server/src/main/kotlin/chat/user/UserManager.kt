package org.example.chat.user

import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import org.example.chat.session.SessionManager
import java.util.concurrent.atomic.AtomicInteger

class UserManager(
    private val sessionManager: SessionManager,
    private val userResolver: UserResolver
) {
    private val userCounter = AtomicInteger(0)

    private val logger = KtorSimpleLogger(this::class.java.simpleName)

    fun userJoinedChat(session: DefaultWebSocketServerSession): User {
        userCounter.getAndIncrement()
        val username = userResolver.resolveUser(session)
        logger.info("User: $username joined chat!")
        sessionManager.addSession(username, session)
        return User(username)
    }

    suspend fun userLeftChat(session: DefaultWebSocketServerSession) {
        sessionManager.removeSession(userResolver.resolveUser(session))
        userCounter.decrementAndGet()
    }

    fun getNumberOfUsersInChat() = userCounter.get()
}