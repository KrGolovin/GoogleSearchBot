package ru.krgolovin.googlesearchbot.commands

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message


final class GetChatIdCommand : MessageCommand {
    override val name: String
        get() = "/chatId"
    override val description: String
        get() = "Say chat id for user"

    override fun onCall(message: Message): SendMessage = with(message) {
        SendMessage(chatId.toString(), chatId.toString())
    }
}