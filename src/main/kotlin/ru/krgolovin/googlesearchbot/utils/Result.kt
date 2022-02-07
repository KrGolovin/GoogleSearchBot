package ru.krgolovin.googlesearchbot.utils

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.krgolovin.googlesearchbot.commands.MessageCommand

sealed interface Result{
    sealed class Error(private val chatId: Long, private val message: String): Result {
        fun getSendMessage() = SendMessage(chatId.toString(), message)
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Error

            if (chatId != other.chatId) return false
            if (message != other.message) return false

            return true
        }

        override fun hashCode(): Int {
            var result = chatId.hashCode()
            result = 31 * result + message.hashCode()
            return result
        }
    }

    data class Success(val command: MessageCommand) : Result
}

class NotCommandError(chatId: Long) : Result.Error(
    chatId, "Enter command. Write /help to show all commands..."
)

class UnknownCommandError(chatId: Long) : Result.Error(
    chatId, "Unknown command, say /help to explore all available commands..."
)