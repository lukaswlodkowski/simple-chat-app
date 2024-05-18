package org.example

import io.ktor.server.application.*
import org.example.plugins.configureRouting
import org.example.plugins.configureSockets

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureSockets()
}