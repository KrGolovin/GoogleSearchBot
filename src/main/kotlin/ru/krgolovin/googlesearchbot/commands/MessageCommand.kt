package ru.krgolovin.googlesearchbot.commands

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import ru.krgolovin.googlesearchbot.api.PlaceholderSearchApi
import ru.krgolovin.googlesearchbot.model.Post

interface MessageCommand {
    val name: String
    val description: String

    fun onCall(message: Message): SendMessage?
}

@Component
final class StartCommand : MessageCommand {
    override val name: String
        get() = "/start"
    override val description: String
        get() = "Start bot"

    override fun onCall(message: Message): SendMessage =
        SendMessage(message.chatId.toString(), "Hello, ${message.from?.userName ?: "anonymous"}")
}

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

@Component
final class PostCommand(@Autowired val placeholderSearchApi: PlaceholderSearchApi) : MessageCommand {
    override val name: String
        get() = "/post"
    override val description: String
        get() = "Get info about post"


    override fun onCall(message: Message): SendMessage? {
        val answer = message.text?.removePrefix(name) ?: return null
        val number =
            answer.trim().toIntOrNull() ?: return SendMessage(message.chatId.toString(), "Incorrect number of post")
        val result = getPostResult(number) ?: return SendMessage(message.chatId.toString(), "Cannot find your post")
        return SendMessage(message.chatId.toString(), result.toString())
    }

    private fun getPostResult(number: Int): Post? {
        val a = placeholderSearchApi.getPost(number).execute()
        return when {
            a.isSuccessful -> a.body()
            else -> null
        }
    }

}
