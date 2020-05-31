package com.tegMob.connectivity
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.models.Game
import retrofit2.Call
import retrofit2.http.*

interface MatchesRouter {

    @Headers("Content-Type: application/json")
    @POST("match")
    fun createMatch(@Body matchInformation: MatchDTOs.MatchCreationDTO): Call<Unit>

    @GET("/games")
    fun getGamesList(): Call<List<Game>>

}