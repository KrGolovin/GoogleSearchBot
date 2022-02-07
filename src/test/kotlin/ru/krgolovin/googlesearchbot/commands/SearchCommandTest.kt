package ru.krgolovin.googlesearchbot.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import retrofit2.Call
import retrofit2.Response
import ru.krgolovin.googlesearchbot.api.GoogleSearchApi
import ru.krgolovin.googlesearchbot.model.QueryResult
import ru.krgolovin.googlesearchbot.model.SearchResult

internal class SearchCommandTest {

    @Test
    fun `onCall with null text`() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { text } doReturn null
        }
        val googleSearchApiMock = mock<GoogleSearchApi> {}

        val searchCommand = SearchCommand(googleSearchApiMock, "")

        val expectedSendMessage = null
        val actualSendMessage = searchCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    @Test
    fun `onCall with empty answer`() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { text } doReturn "/search   "
        }
        val googleSearchApiMock = mock<GoogleSearchApi> {}
        val searchCommand = SearchCommand(googleSearchApiMock, "")

        val expectedSendMessage = SendMessage("1", "Nothing to search")
        val actualSendMessage = searchCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    @Test
    fun `onCall with unsuccessful query `() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { text } doReturn "/search a"
        }
        val responseMock = mock<Response<QueryResult>> {
            on {isSuccessful} doReturn false
        }
        val callQueryResultMock = mock<Call<QueryResult>> {
            on { execute() } doReturn responseMock
        }
        val googleSearchApiMock = mock<GoogleSearchApi> {
            on {getSearchResult("a", "")} doReturn callQueryResultMock
        }

        val searchCommand = SearchCommand(googleSearchApiMock, "")

        val expectedSendMessage = SendMessage("1", "Cannot find your query")
        val actualSendMessage = searchCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    @Test
    fun `onCall with error query result `() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { text } doReturn "/search gifjfjjfjf"
        }
        val queryResult = QueryResult(null, "error")
        val responseMock = mock<Response<QueryResult>> {
            on {isSuccessful} doReturn true
            on {body()} doReturn queryResult
        }
        val callQueryResultMock = mock<Call<QueryResult>> {
            on { execute() } doReturn responseMock
        }
        val googleSearchApiMock = mock<GoogleSearchApi> {
            on {getSearchResult("gifjfjjfjf", "")} doReturn callQueryResultMock
        }

        val searchCommand = SearchCommand(googleSearchApiMock, "")

        val expectedSendMessage = SendMessage("1", "Cannot find your query")
        val actualSendMessage = searchCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    @Test
    fun `onCall with normal query result `() {
        val title = "github"
        val link = "https://github.com"
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { text } doReturn "/search $title"
        }
        val queryResult = QueryResult(listOf(SearchResult(title, link)), null)
        val responseMock = mock<Response<QueryResult>> {
            on {isSuccessful} doReturn true
            on {body()} doReturn queryResult
        }
        val callQueryResultMock = mock<Call<QueryResult>> {
            on { execute() } doReturn responseMock
        }
        val googleSearchApiMock = mock<GoogleSearchApi> {
            on {getSearchResult(title, "")} doReturn callQueryResultMock
        }

        val searchCommand = SearchCommand(googleSearchApiMock, "")

        val expectedSendMessage = SendMessage("1", "title:\n${title}\nlink:\n${link}")
        val actualSendMessage = searchCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }
}
