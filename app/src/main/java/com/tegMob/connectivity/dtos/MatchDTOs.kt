package com.tegMob.connectivity.dtos

import com.tegMob.models.Player

object MatchDTOs {
    data class MatchCreationDTO(val matchname: String, val owner: String, val size: Int)

    data class MatchCreationResponseDTO(val id: String)

    data class MatchPlayerRemoveDTO(val user_to_remove: String)

    data class MatchPlayerDTO(val color: String, val user: String)
    
    data class MatchListItemDTO(val id: String, val matchname: String, val owner: String, val size: Int, val players: List<MatchPlayerDTO>, val stage: String)

    data class MatchPlayerAddDTO(val user_to_add: String)
}