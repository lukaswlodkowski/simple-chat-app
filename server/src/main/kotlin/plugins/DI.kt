package org.example.plugins

import io.ktor.server.application.*
import org.example.plugins.di.appModule
import org.koin.ktor.plugin.Koin

fun Application.configureDI() {
    install(Koin) {
        modules(appModule)
    }
}