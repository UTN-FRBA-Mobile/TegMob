package com.tegMob.models

import com.tegMob.connectivity.dtos.MatchDTOs

data class Game(
    val name: String,
    val description: String,
    val full: Boolean
)

object RandomGames {

    val GAMES_LIST_DTO: List<MatchDTOs.MatchListItemDTO> = listOf(
        MatchDTOs.MatchListItemDTO(id = "1", matchname = "juego1", owner = "user1", size = 3, players = listOf(), stage = "CREATED"),
        MatchDTOs.MatchListItemDTO(id = "2", matchname = "juego2", owner = "user2", size = 3, players = listOf(), stage = "CREATED"),
        MatchDTOs.MatchListItemDTO(id = "3", matchname = "juego3", owner = "user3", size = 3, players = listOf(), stage = "CREATED"),
        MatchDTOs.MatchListItemDTO(id = "4", matchname = "juego4", owner = "user4", size = 3, players = listOf(), stage = "CREATED")
    )

}

