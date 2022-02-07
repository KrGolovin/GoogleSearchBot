package ru.krgolovin.googlesearchbot.configuraion

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import retrofit2.Retrofit
import ru.krgolovin.googlesearchbot.api.GoogleSearchApi


@Configuration
class GoogleSearchApiProvider {
    @ExperimentalSerializationApi
    @Bean
    @Scope("singleton")
    fun getGoogleSearchApi(): GoogleSearchApi {
        val contentType = "application/json".toMediaType()
        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }
        return Retrofit.Builder().baseUrl(serverUrl).addConverterFactory(json.asConverterFactory(contentType)).build()
            .create(GoogleSearchApi::class.java)
    }

    companion object {
        private const val serverUrl = "http://serpapi.com/"
    }
}
