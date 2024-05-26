package org.example.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.example.chat.history.model.ConversationHistoryTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val driverClass =
        if (developmentMode) "org.h2.Driver"
        else environment.config.property("storage.driverClassName").getString()
    val jdbcUrl =
        if (developmentMode) "jdbc:h2:file:./build/db;DATABASE_TO_UPPER=false;MODE=POSTGRESQL"
        else environment.config.property("storage.jdbcURL").getString()

    val db = Database.connect(provideDataSource(jdbcUrl, driverClass))

    transaction(db) {
        SchemaUtils.create(ConversationHistoryTable)
    }
}

private fun provideDataSource(url: String, driverClass: String): HikariDataSource {
    val hikariConfig = HikariConfig().apply {
        driverClassName = driverClass
        jdbcUrl = url
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    return HikariDataSource(hikariConfig)
}

suspend fun <T> dbQuery(block: suspend () -> T): T {
    return newSuspendedTransaction(Dispatchers.IO) { block() }
}