package ru.krgolovin.googlesearchbot.commands

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import ru.krgolovin.googlesearchbot.api.GoogleSearchApi
import ru.krgolovin.googlesearchbot.model.SearchResult

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
final class SearchCommand(
    @Autowired val googleSearchApi: GoogleSearchApi,
    @Value("\${serpapi.key}") private val apiKey: String,
) : MessageCommand {
    override val name: String
        get() = "/search"
    override val description: String
        get() = "Get first result in google about this query"


    override fun onCall(message: Message): SendMessage? {
        val answer = message.text?.removePrefix(name) ?: return null
        if (answer.isEmpty()) return SendMessage(message.chatId.toString(), "Nothing to search")
        val result = getSearchResult(answer) ?: return SendMessage(message.chatId.toString(), "Cannot find your query")
        return SendMessage(message.chatId.toString(), "title:\n${result.title}\nlink:\n${result.link}")
    }

    private fun getSearchResult(query: String): SearchResult? {
        val queryResultResponse = googleSearchApi.getSearchResult(query, apiKey).execute()
        return when {
            queryResultResponse.isSuccessful ->
                queryResultResponse.body()?.result?.getOrNull(0)
            else -> null
        }
    }

}
