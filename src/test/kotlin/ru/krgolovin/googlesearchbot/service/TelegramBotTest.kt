package ru.krgolovin.googlesearchbot.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import ru.krgolovin.googlesearchbot.api.GoogleSearchApi
import ru.krgolovin.googlesearchbot.commands.HelpCommand
import ru.krgolovin.googlesearchbot.commands.SayCommand
import ru.krgolovin.googlesearchbot.commands.SearchCommand
import ru.krgolovin.googlesearchbot.commands.StartCommand
import ru.krgolovin.googlesearchbot.utils.NotCommandError
import ru.krgolovin.googlesearchbot.utils.Result
import ru.krgolovin.googlesearchbot.utils.UnknownCommandError

internal class TelegramBotTest {

    @Test
    fun `getMessageResult with no command`() {
        val chatId = 1L
        val text = "hello"
        val telegramBot = TelegramBot("", "", listOf())

        val expectedResult = NotCommandError(chatId)
        val actualResult = telegramBot.getMessageResult(chatId, text)

        Assertions.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `getMessageResult with unknown command`() {
        val chatId = 1L
        val text = "/hello"
        val googleSearchApiMock = mock<GoogleSearchApi> {}
        val messageCommands = listOf(
            SayCommand(), StartCommand(), SearchCommand(googleSearchApiMock, ""), HelpCommand(
                listOf()
            )
        )
        val telegramBot = TelegramBot("", "", messageCommands)


        val expectedResult = UnknownCommandError(chatId)
        val actualResult = telegramBot.getMessageResult(chatId, text)

        Assertions.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `getMessageResult with normal command`() {
        val chatId = 1L
        val text = "/start"
        val googleSearchApiMock = mock<GoogleSearchApi> {}
        val startCommand = StartCommand()
        val messageCommands = listOf(
            SayCommand(), startCommand, SearchCommand(googleSearchApiMock, ""), HelpCommand(
                listOf()
            )
        )
        val telegramBot = TelegramBot("", "", messageCommands)

        val expectedResult = Result.Success(startCommand)
        val actualResult = telegramBot.getMessageResult(chatId, text)

        Assertions.assertEquals(expectedResult, actualResult)
    }
}
