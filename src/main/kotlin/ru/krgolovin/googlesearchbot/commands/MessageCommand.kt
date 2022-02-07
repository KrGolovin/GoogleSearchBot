package ru.krgolovin.googlesearchbot.commands

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

interface MessageCommand {
    val name: String
    val description: String

    fun onCall(message: Message): SendMessage?
}
