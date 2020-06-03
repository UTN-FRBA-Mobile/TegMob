package com.tegMob.connectivity.dtos

object MatchDTOs {
    data class MatchCreationDTO(val name: String, val owner: String, val size: Int)

    data class MatchPlayerRemoveDTO(val user_to_remove: String)
    
    data class MatchListItem(val description: String, val name: String, val owner: String, val size: Int, val socket: String)

}