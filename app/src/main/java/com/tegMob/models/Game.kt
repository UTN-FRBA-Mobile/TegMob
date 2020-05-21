package com.tegMob.models

data class Game(
    val name: String,
    val description: String,
    val full: Boolean
)

object RandomGames {

    val gamesList: List<Game> = listOf(
        Game("juego1", "descrip1", false),
        Game("juego2", "descrip2", false),
        Game("juego3", "descrip3", false),
        Game("juego4", "descrip4", false)
    )

}

