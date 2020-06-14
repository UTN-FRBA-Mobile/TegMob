package com.tegMob.connectivity

import retrofit2.converter.gson.GsonConverterFactory

object TegMobClient {
    private const val tegMobUrl = "http://tegmobapp.eba-2psmvpsn.us-east-1.elasticbeanstalk.com/"

    fun <T> buildService(service: Class<T>): T {
        return retrofit2.Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(tegMobUrl)
            .build()
            .create(service)
    }
}