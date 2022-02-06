package ru.krgolovin.googlesearchbot.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.krgolovin.googlesearchbot.model.QueryResult

interface GoogleSearchApi {
    @GET("search.json?engine=google")
    fun getSearchResult(@Query("q") query: String, @Query("api_key") apiKey: String): Call<QueryResult>
}
