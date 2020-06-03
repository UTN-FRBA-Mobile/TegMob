package com.tegMob.connectivity

import com.tegMob.connectivity.dtos.UserDTOs
import com.tegMob.models.Player
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlayersRouter {

    @GET("{player_id}")
    fun getPlayer(@Path("player_id") playerId : Int): Call<Player>
}