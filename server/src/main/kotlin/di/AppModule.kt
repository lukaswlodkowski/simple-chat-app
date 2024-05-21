package org.example.plugins.di

import org.example.chat.SessionManager
import org.example.chat.command.MessageCommandProcessor
import org.example.chat.history.HistoryRepository
import org.example.chat.history.HistoryService
import org.koin.dsl.module

val appModule = module {
    single<HistoryRepository> {
        HistoryRepository()
    }
    single<HistoryService> {
        HistoryService(historyRepository = get())
    }
    single<SessionManager> {
        SessionManager()
    }
    single<MessageCommandProcessor> {
        MessageCommandProcessor(sessionManager = get())
    }

}