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

internal class SayCommandTest {
    @Test
    fun `onCall with null text`() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { text } doReturn null
        }

        val sayCommand = SayCommand()

        val expectedSendMessage = null
        val actualSendMessage = sayCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    @Test
    fun `onCall with empty answer`() {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { text } doReturn "/say   "
        }

        val sayCommand = SayCommand()

        val expectedSendMessage = SendMessage("1", "You didn't say anything")
        val actualSendMessage = sayCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    fun `onCall with normal text`(testData: TestData) {
        val mockMessage = mock<Message> {
            on { chatId } doReturn 1
            on { text } doReturn testData.text
        }

        val sayCommand = SayCommand()

        val expectedSendMessage = SendMessage("1", testData.answer)
        val actualSendMessage = sayCommand.onCall(mockMessage)

        Assertions.assertEquals(actualSendMessage, expectedSendMessage)
    }

    companion object {
        data class TestData(val text: String, val answer: String)

        @JvmStatic
        private fun getTestData() = listOf(
            Arguments.of(TestData("/sayHello", "Hello")),
            Arguments.of(TestData("/say Hello", "Hello")),
            Arguments.of(TestData("/say\uD83D\uDE0A\uD83D\uDE0A\uD83D\uDE0A", "\uD83D\uDE0A\uD83D\uDE0A\uD83D\uDE0A"))
        )
    }
}
