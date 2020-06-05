package com.tegMob.connectivity.dtos

object UserDTOs {
    data class UserToAddDTO(val userid: String)
    data class UserLogin(val username: String, val password : String)
    data class NewUser (var username : String, var password : String,
        var firstname : String, var lastname : String)
}