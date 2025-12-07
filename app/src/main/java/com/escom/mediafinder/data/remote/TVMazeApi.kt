package com.escom.mediafinder.data.remote

import com.escom.mediafinder.data.model.Show
import com.escom.mediafinder.data.model.ShowSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVMazeApi {
    @GET("search/shows")
    suspend fun searchShows(@Query("q") query: String): List<ShowSearchResponse>

    @GET("shows/{id}")
    suspend fun getShowById(@Path("id") id: Int): Show

    @GET("shows")
    suspend fun getAllShows(@Query("page") page: Int = 0): List<Show>
}