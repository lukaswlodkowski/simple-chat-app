package di

import org.example.chat.SessionManager
import org.example.chat.command.ExitCommand
import org.example.chat.command.KickCommand
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
    single<ExitCommand> {
        ExitCommand(sessionManager = get())
    }
    single<KickCommand> {
        KickCommand(sessionManager = get())
    }
    single<MessageCommandProcessor> {
        MessageCommandProcessor(exitCommand = get(), kickCommand = get())
    }

}