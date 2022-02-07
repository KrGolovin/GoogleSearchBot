package ru.krgolovin.googlesearchbot.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Component
final class StartCommand : MessageCommand {
    override val name: String
        get() = "/start"
    override val description: String
        get() = "Start bot"

    override fun onCall(message: Message): SendMessage =
        SendMessage(message.chatId.toString(), "Hello, ${message.from?.userName ?: "anonymous"}")
}
