package com.tegMob.connectivity.routers
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.models.Game
import retrofit2.Call
import retrofit2.http.*

interface MatchesRouter {

    @Headers("Content-Type: application/json")
    @POST("match")
    fun createMatch(@Body matchInformation: MatchDTOs.MatchCreationDTO): Call<Unit>

    @GET("/games")
    fun getGamesList(): Call<List<MatchDTOs.MatchListItem>>

    @Headers("Content-Type: application/json")
    @PUT("match/leave/{match_id}")
    fun removePlayer(@Path("match_id") matchId: Int, @Body removedPlayer: MatchDTOs.MatchPlayerRemoveDTO): Call<Unit>

    @Headers("Content-Type: application/json")
    @PUT("match/join/{match_id}")
    fun addPlayer(@Path("match_id") matchId: Int, @Body addedPlayer: MatchDTOs.MatchPlayerAddDTO): Call<Unit>


}