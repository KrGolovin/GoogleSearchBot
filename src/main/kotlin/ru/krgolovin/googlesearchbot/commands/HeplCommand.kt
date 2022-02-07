package ru.krgolovin.googlesearchbot.commands

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Component
final class HelpCommand(@Autowired messageCommands: List<MessageCommand>) : MessageCommand {
    override val name: String
        get() = "/help"
    override val description: String
        get() = "Show all commands"

    private val messageCommands: List<MessageCommand> = listOf(this) + messageCommands

    override fun onCall(message: Message): SendMessage = SendMessage(message.chatId.toString(),
        messageCommands.joinToString(separator = "\n") { String.format("%-10s - %s", it.name, it.description) })
}
