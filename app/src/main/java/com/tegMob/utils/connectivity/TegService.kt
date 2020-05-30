package com.tegMob.utils.connectivity

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TegService {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://e9c4x73vla.execute-api.us-east-1.amazonaws.com/dev/")
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}