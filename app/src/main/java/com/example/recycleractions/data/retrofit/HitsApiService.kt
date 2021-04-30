package com.example.recycleractions.data.retrofit

import com.example.recycleractions.domain.entities.HitResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HitsApiService {

    @GET("search_by_date/")
    suspend fun getHits(@Query("query") query: String,
                        @Query("page") page: Int): HitResponse
}