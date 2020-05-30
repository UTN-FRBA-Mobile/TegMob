package com.tegMob.clients
import retrofit2.converter.gson.GsonConverterFactory

object ClientBuilder {
    object MatchesClientBuilder {
        private const val matchesUrl = "https://e9c4x73vla.execute-api.us-east-1.amazonaws.com/dev/"
        private val retrofit = retrofit2.Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(matchesUrl)
            .build()

        fun<T> buildService(service: Class<T>): T{
            return retrofit.create(service)
        }
    }
}