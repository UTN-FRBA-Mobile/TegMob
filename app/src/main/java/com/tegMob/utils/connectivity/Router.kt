package com.tegMob.utils.connectivity

import com.tegMob.models.Game
import com.tegMob.models.Player
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Router {

    @GET("/games")
    fun getGamesList(): Call<List<Game>>

    @GET("/player")
    fun getPlayer(@Query("player_id") playerId : Int): Call<Player>

}