package com.tegMob.connectivity.dtos

object UserDTOs {
    data class UserToAddDTO(val userid: String)
    data class UserLogin(val username: String, val password: String)
    data class NewUser(
        val username: String, val password: String,
        val firstName: String, val lastName: String
    )
    data class LoggedUserResponseDTO(val id: String)
}