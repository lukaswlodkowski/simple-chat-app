package org.example.plugins

import io.ktor.server.application.*
import di.appModule
import org.koin.ktor.plugin.Koin

fun Application.configureDI() {
    install(Koin) {
        modules(appModule)
    }
}