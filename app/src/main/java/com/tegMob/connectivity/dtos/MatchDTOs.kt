package com.tegMob.connectivity.dtos

object MatchDTOs {
    data class MatchCreationDTO(val name: String, val owner: String, val size: Int)

    data class MatchPlayerRemoveDTO(val user_to_remove: String)
    
    data class MatchListItem(val description: String, val id: Int, val name: String, val owner: String, val size: Int, val socket: String)

    data class MatchPlayerAddDTO(val user_to_add: String)
}