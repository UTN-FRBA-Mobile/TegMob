package com.tegMob.models

import com.tegMob.connectivity.dtos.MatchDTOs

data class Game(
    val name: String,
    val description: String,
    val full: Boolean
)

object RandomGames {

    val gamesList: List<MatchDTOs.MatchListItem> = listOf(
        MatchDTOs.MatchListItem(id = 1, description = "primer juego", name = "juego1", owner = "user1", size = 3, socket = ""),
        MatchDTOs.MatchListItem(id = 2, description = "segundo juego", name = "juego2", owner = "user2", size = 3, socket = ""),
        MatchDTOs.MatchListItem(id = 3, description = "tercer juego", name = "juego3", owner = "user3", size = 3, socket = ""),
        MatchDTOs.MatchListItem(id = 4, description = "cuarto juego", name = "juego4", owner = "user4", size = 3, socket = "")
    )

}

