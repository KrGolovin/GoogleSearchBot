package ru.krgolovin.googlesearchbot.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import retrofit2.Retrofit
import ru.krgolovin.googlesearchbot.api.PlaceholderSearchApi


@Configuration
class PlaceholderSearchApiProvider {
    @ExperimentalSerializationApi
    @Bean
    @Scope("singleton")
    fun getPlaceholderSearchApi(): PlaceholderSearchApi {
        val contentType = "application/json".toMediaType()
        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder().baseUrl(serverUrl).addConverterFactory(json.asConverterFactory(contentType)).build()
            .create(PlaceholderSearchApi::class.java)
    }

    companion object {
        private const val serverUrl = "https://jsonplaceholder.typicode.com/"
    }
}
