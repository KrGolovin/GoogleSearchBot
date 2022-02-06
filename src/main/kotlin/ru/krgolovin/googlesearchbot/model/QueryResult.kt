package ru.krgolovin.googlesearchbot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryResult(
    @SerialName("organic_results")
    val result: List<SearchResult>?,
    @SerialName("error")
    val error: String?
)

@Serializable
data class SearchResult(
    @SerialName("title")
    val title: String,
    @SerialName("link")
    val link: String
)
