package com.tegMob.connectivity

import com.tegMob.models.*
import retrofit2.Call
import retrofit2.http.*

interface UsersRouter {

    @GET("/games")
    fun getGamesList(): Call<List<Game>>

    @POST("users")
    fun createUser(@Body userData: User): Call<Unit>

}