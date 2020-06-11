package com.tegMob.connectivity

import retrofit2.converter.gson.GsonConverterFactory

object TegMobClient {
    private const val tegMobUrl = "https://tzdb0jz1yh.execute-api.us-east-1.amazonaws.com/dev/"

    fun <T> buildService(service: Class<T>): T {
        return retrofit2.Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(tegMobUrl)
            .build()
            .create(service)
    }
}