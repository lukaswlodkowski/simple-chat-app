package org.example.chat.history

class HistoryService(private val historyRepository: HistoryRepository) {
    suspend fun createHistoryLog(username: String, message: String) =
        historyRepository.createHistoryLog(username, message)

    suspend fun getHistoryLog(numberOfPreviousMessages: Int) = historyRepository.getHistoryLogs(numberOfPreviousMessages)
}