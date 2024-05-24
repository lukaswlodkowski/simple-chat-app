package org.example

import io.ktor.server.application.*
import org.example.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureAuth()
    configureDI()
    configureSockets()
    configureRouting()
    configureDatabases()
}