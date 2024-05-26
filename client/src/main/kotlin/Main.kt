package org.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

//TODO: when server shuts down, try to reconnect to it whenever available.
fun main(args: Array<String>) {
    val client = HttpClient {
        install(WebSockets)
    }
    val token = generateToken(args.first())
    println("Token $token")
    runBlocking {
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/chat/${args[1]}", {
            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )
        }) {
            val messageOutputRoutine = launch { outputMessages() }
            val userInputRoutine = launch { inputMessages() }

            userInputRoutine.join()
            messageOutputRoutine.cancelAndJoin()
        }
    }
    client.close()
    println("Connection closed. Goodbye!")
}

//TODO: Replace with Auth Server
fun generateToken(username: String): String = JWT.create()
    .withSubject("Authentication")
    .withIssuer("http://0.0.0.0:8080/")
    .withClaim("username", username)
    .withAudience("http://0.0.0.0:8080/")
    .withExpiresAt(Date(System.currentTimeMillis() + 36_000_00 * 24))
    .sign(Algorithm.HMAC256("secret"))

suspend fun DefaultClientWebSocketSession.outputMessages() {
    try {
        for (message in incoming) {
            message as? Frame.Text ?: continue
            println(message.readText())
        }
    } catch (e: Exception) {
        println("Error while receiving: " + e.localizedMessage)
    }
}

suspend fun DefaultClientWebSocketSession.inputMessages() {
    while (true) {
        val message = readLine() ?: ""
        if (message.equals("exit", true)) return
        try {
            send(message)
        } catch (e: Exception) {
            println("Error while sending: " + e.localizedMessage)
            return
        }
    }
}