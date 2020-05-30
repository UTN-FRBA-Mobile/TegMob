package com.tegMob.clients
import retrofit2.converter.gson.GsonConverterFactory

object ClientBuilder {
    object MatchesClientBuilder {
        private const val usersUrl = "http://demo9888747.mockable.io/"
        private val retrofit = retrofit2.Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(usersUrl)
            .build()

        fun<T> buildService(service: Class<T>): T{
            return retrofit.create(service)
        }
    }
}