package org.example.chat.user

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.websocket.*
import io.ktor.util.logging.*

class UserResolver {
    private val logger = KtorSimpleLogger(this::class.java.simpleName)
    fun resolveUser(session: DefaultWebSocketServerSession): User {
        val principal = session.call.principal<JWTPrincipal>()
        logger.debug("Principal: $principal")
        return User(principal!!.payload.getClaim("username").asString())
    }
}