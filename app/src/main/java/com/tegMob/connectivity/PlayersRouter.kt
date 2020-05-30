package com.tegMob.connectivity

import com.tegMob.models.Player
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PlayersRouter {

    @GET("{player_id}")
    fun getPlayer(@Path("player_id") playerId : Int): Call<Player>
}