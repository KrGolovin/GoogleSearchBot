package ru.krgolovin.googlesearchbot.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.krgolovin.googlesearchbot.model.Post

interface PlaceholderSearchApi {
    @GET("/posts/{number}")
    fun getPost(@Path("number") number: Int): Call<Post>
}
