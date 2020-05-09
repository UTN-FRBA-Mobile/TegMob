package com.tegMob.utils.connectivity

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TegService {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("127.0.0.1")
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}