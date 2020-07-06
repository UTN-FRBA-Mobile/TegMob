package com.tegMob.connectivity.routers
import com.tegMob.connectivity.dtos.MatchDTOs
import retrofit2.Call
import retrofit2.http.*

interface MatchesRouter {

    @Headers("Content-Type: application/json")
    @POST("matchs/create")
    fun createMatch(@Body matchInformation: MatchDTOs.MatchCreationDTO): Call<MatchDTOs.MatchCreationResponseDTO>

    @GET("matchs/")
    fun getGamesList(): Call<List<MatchDTOs.MatchListItemDTO>>

    @GET("matchs/{match_id}")
    fun getMatchById(@Path("match_id") matchId: String): Call<MatchDTOs.MatchListItemDTO>

    @Headers("Content-Type: application/json")
    @PUT("matchs/leave/{match_id}")
    fun removePlayer(@Path("match_id") matchId: String, @Body removedPlayer: MatchDTOs.MatchPlayerRemoveDTO): Call<Unit>

    @Headers("Content-Type: application/json")
    @PUT("matchs/join/{match_id}")
    fun addPlayer(@Path("match_id") matchId: String, @Body addedPlayer: MatchDTOs.MatchPlayerAddDTO): Call<Unit>


}