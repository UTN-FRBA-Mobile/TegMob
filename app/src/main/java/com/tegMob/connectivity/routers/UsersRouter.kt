package com.tegMob.connectivity.routers

import com.tegMob.connectivity.dtos.UserDTOs
import com.tegMob.models.*
import retrofit2.Call
import retrofit2.http.*

interface UsersRouter {

    @POST("users/register")
    fun createUser(@Body userData: UserDTOs.NewUser): Call<Unit>

    @POST("users/authenticate")
    fun loginUser(@Body loginData : UserDTOs.UserLogin): Call<UserDTOs.LoggedUserResponseDTO>
}