package org.example.history

class HistoryService(private val historyRepository: HistoryRepository) {
    suspend fun createHistoryLog(message: String) =
        historyRepository.createHistoryLog(message)
}