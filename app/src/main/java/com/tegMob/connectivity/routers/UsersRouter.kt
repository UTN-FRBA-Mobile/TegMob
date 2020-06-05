package com.tegMob.connectivity.routers

import com.tegMob.connectivity.dtos.UserDTOs
import com.tegMob.models.*
import retrofit2.Call
import retrofit2.http.*

interface UsersRouter {

    @POST("users")
    fun createUser(@Body userData: UserDTOs.NewUser): Call<Unit>

    @POST("/users/login")
    fun loginUser(@Body loginData : UserDTOs.UserLogin): Call<Unit>
}