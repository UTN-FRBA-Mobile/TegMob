package com.tegMob.clients.dtos

object MatchDTOs {
    data class MatchCreationDTO(val name: String, val owner: String, val size: Int)
}