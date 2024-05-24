package di

import org.example.chat.ChatHandler
import org.example.chat.MessageSender
import org.example.chat.command.CommandProcessor
import org.example.chat.command.ExitCommand
import org.example.chat.command.KickCommand
import org.example.chat.command.MessageCommandProcessor
import org.example.chat.history.HistoryRepository
import org.example.chat.history.HistoryService
import org.example.chat.session.SessionManager
import org.example.chat.user.UserManager
import org.example.chat.user.UserResolver
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
    single<UserResolver> {
        UserResolver()
    }
    single<MessageSender> {
        MessageSender(sessionManager = get())
    }
    single<UserManager> {
        UserManager(sessionManager = get(), userResolver = get())
    }
    single<CommandProcessor> {
        MessageCommandProcessor(exitCommand = get(), kickCommand = get())
    }
    single<ChatHandler> {
        ChatHandler(
            historyService = get(),
            commandProcessor = get(),
            messageSender = get(),
            userManager = get()
        )
    }
}