package com.tegMob.connectivity.dtos

object MatchDTOs {
    data class MatchCreationDTO(val name: String, val owner: String, val size: Int)

    data class MatchPlayerRemoveDTO(val user_to_remove: String)
}