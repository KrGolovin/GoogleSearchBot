package ru.krgolovin.googlesearchbot.service

import org.jetbrains.annotations.TestOnly
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.krgolovin.googlesearchbot.commands.MessageCommand
import ru.krgolovin.googlesearchbot.utils.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock


@Component
class TelegramBot(
    @Value("\${bot.username}") private val username: String,
    @Value("\${bot.token}") private val token: String,
    private val messageCommands: List<MessageCommand>
) : TelegramLongPollingBot() {
    override fun getBotToken(): String = token

    override fun getBotUsername(): String = username

    override fun onUpdateReceived(update: Update) {
        when {
            update.hasMessage() -> {
                onMessageReceived(update.message ?: return)
            }
        }
    }

    @TestOnly
    fun getMessageResult(chatId: Long, text: String): Result = when {
        text.startsWith("/") -> {
            val currentCommand = messageCommands.firstOrNull { messageCommand -> text.startsWith(messageCommand.name) }
            if (currentCommand == null) {
                UnknownCommandError(chatId)
            } else {
                Result.Success(currentCommand)
            }
        }
        else -> NotCommandError(chatId)
    }

    private fun onMessageReceived(message: Message) {
        val chatId = message.chatId
        val text = if (message.hasText()) message.text else return
        when (val result: Result = getMessageResult(chatId, text)) {
            is Result.Error -> execute(result.getSendMessage())
            is Result.Success -> {
                pool.execute {
                    try {
                        val response = result.command.onCall(message)
                        executeLock.lock()
                        if (response != null) execute(response)
                    } catch (e: Exception) {
                        val messageToSend = SendMessage(chatId.toString(), text)
                        execute(messageToSend)
                    } finally {
                        executeLock.unlock()
                    }
                }
            }
        }
    }

    private val pool: ExecutorService = Executors.newFixedThreadPool(16)

    private val executeLock = ReentrantLock()
}
