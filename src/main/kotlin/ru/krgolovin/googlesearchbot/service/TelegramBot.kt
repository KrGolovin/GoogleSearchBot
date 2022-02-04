package ru.krgolovin.googlesearchbot.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.krgolovin.googlesearchbot.commands.MessageCommand


@Service
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

    private fun onMessageReceived(message: Message) {
        val chatId = message.chatId
        val text = if (message.hasText()) message.text else return
        when {
            text.startsWith("/") -> {
                val currentCommand =
                    messageCommands.firstOrNull { messageCommand -> text.startsWith(messageCommand.name) }
                if (currentCommand == null) {
                    sendMessage(
                        chatId, "Unknown command, say /help to explore all available commands..."
                    )
                } else execute(currentCommand.onCall(message) ?: return)
            }
            else -> {
                sendMessage(chatId, "Enter command. Write /help to show all commands...")
            }
        }
    }

    private fun sendMessage(chatId: Long, text: String) {
        val messageToSend = SendMessage(chatId.toString(), text)
        execute(messageToSend)
    }
}

