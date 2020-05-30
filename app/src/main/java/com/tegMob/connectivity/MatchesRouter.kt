package com.tegMob.connectivity
import com.tegMob.connectivity.dtos.MatchDTOs
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MatchesRouter {
    @Headers("Content-Type: application/json")
    @POST("match")
    fun createMatch(@Body matchInformation: MatchDTOs.MatchCreationDTO): Call<Unit>
}