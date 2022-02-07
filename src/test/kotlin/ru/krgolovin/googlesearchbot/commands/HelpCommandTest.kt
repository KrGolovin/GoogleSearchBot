package ru.krgolovin.googlesearchbot.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.telegram.telegrambots.meta.api.objects.Message
import ru.krgolovin.googlesearchbot.api.GoogleSearchApi

internal class HelpCommandTest {

    @Test
    fun `onCall with only help command`() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
        }
        val messageCommands = listOf<MessageCommand>()
        val helpCommand = HelpCommand(messageCommands)

        val expectedContainsStrings = listOf(helpCommand.name, helpCommand.description)
        val actualSendMessage = helpCommand.onCall(mockMessage)

        Assertions.assertTrue(expectedContainsStrings.all { str ->
            actualSendMessage.text.contains(str)
        })
    }

    @Test
    fun `onCall with all commands`() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
        }
        val googleSearchApiMock = mock<GoogleSearchApi> {}
        val messageCommands = listOf(SayCommand(), StartCommand(), SearchCommand(googleSearchApiMock, ""))
        val helpCommand = HelpCommand(messageCommands)

        val expectedContainsStrings = buildList {
            messageCommands.forEach { command ->
                add(command.name)
                add(command.description)
            }
            add(helpCommand.name)
            add(helpCommand.description)
        }
        val actualSendMessage = helpCommand.onCall(mockMessage)

        Assertions.assertTrue(expectedContainsStrings.all { str ->
            actualSendMessage.text.contains(str)
        })
    }
}
