package com.tegMob.utils.connectivity

import com.tegMob.models.*
import retrofit2.Call
import retrofit2.http.*

interface Router {

    @GET("/games")
    fun getGamesList(): Call<List<Game>>

    @GET("/player")
    fun getPlayer(@Query("player_id") playerId : Int): Call<Player>

    @POST("users")
    fun createUser(@Body userData: User): Call<Unit>

}