package ru.krgolovin.googlesearchbot.commands

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import ru.krgolovin.googlesearchbot.api.GoogleSearchApi
import ru.krgolovin.googlesearchbot.model.SearchResult

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
