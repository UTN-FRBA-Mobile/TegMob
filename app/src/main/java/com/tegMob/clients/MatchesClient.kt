package com.tegMob.clients
import com.tegMob.clients.dtos.MatchDTOs
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MatchesClient {
    @Headers("Content-Type: application/json")
    @POST("match")
    fun createMatch(@Body matchInformation: MatchDTOs.MatchCreationDTO): Call<Unit>
}