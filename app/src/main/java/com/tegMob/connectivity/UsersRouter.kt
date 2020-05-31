package com.tegMob.connectivity

import com.tegMob.models.*
import retrofit2.Call
import retrofit2.http.*

interface UsersRouter {

    @POST("users")
    fun createUser(@Body userData: User): Call<Unit>

}