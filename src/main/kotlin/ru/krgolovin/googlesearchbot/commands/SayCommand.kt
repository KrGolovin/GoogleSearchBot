package ru.krgolovin.googlesearchbot.commands

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Component
final class SayCommand : MessageCommand {
    override val name: String
        get() = "/say"
    override val description: String
        get() = "Say what you say"

    override fun onCall(message: Message): SendMessage? {
        val responseText = message.text?.removePrefix(name) ?: return null
        if (responseText.isEmpty()) return SendMessage(message.chatId.toString(), "You didn't say anything")
        return SendMessage(message.chatId.toString(), responseText)
    }
}
