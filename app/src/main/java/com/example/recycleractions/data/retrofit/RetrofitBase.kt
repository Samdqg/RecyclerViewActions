package com.example.recycleractions.data.retrofit

import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBase {
    companion object{
        private const val BASE_URL = "https://hn.algolia.com/api/v1/"

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(getHttpClient())
                .build()
        }

        private fun getHttpClient(): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
            val credentials = Credentials.basic("test", "Fcb7mPap")
            httpClient.addInterceptor {
                val request = it.request().newBuilder()
                        .addHeader("Authorization", credentials).build()
                it.proceed(request)
            }.connectTimeout(15, TimeUnit.MINUTES)
                    .readTimeout(15, TimeUnit.MINUTES)
                    .writeTimeout(15, TimeUnit.MINUTES)

            return httpClient.build()
        }
    }
}