package ru.krgolovin.googlesearchbot.commands

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User

internal class StartCommandTest {
    @Test
    fun `onCall with null from`() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { from } doReturn null
        }

        val startCommand = StartCommand()

        val expectedSendMessage = SendMessage("1", "Hello, anonymous")
        val actualSendMessage = startCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    @Test
    fun `onCall with null username`() {
        val mockUser = mock<User> {
            on { userName } doReturn null
        }

        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { from } doReturn mockUser
        }

        val startCommand = StartCommand()

        val expectedSendMessage = SendMessage("1", "Hello, anonymous")
        val actualSendMessage = startCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    @ParameterizedTest
    @MethodSource("getUsernames")
    fun `onCall with normal username`(username: String) {
        val mockUser = mock<User> {
            on { userName } doReturn username
        }

        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { from } doReturn mockUser
        }

        val startCommand = StartCommand()

        val expectedSendMessage = SendMessage("1", "Hello, $username")
        val actualSendMessage = startCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }


    companion object {
        @JvmStatic
        private fun getUsernames() = listOf(
            Arguments.of("KrGolovin"), Arguments.of("Ikeaieryn"), Arguments.of("Hakimov")
        )
    }
}
