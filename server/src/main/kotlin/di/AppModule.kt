package org.example.plugins.di

import org.example.history.HistoryRepository
import org.example.history.HistoryService
import org.koin.dsl.module

val appModule = module {
    single<HistoryRepository> {
        HistoryRepository()
    }
    single<HistoryService> {
        HistoryService(get())
    }
}