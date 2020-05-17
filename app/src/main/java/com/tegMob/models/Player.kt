package com.tegMob.models

data class Player
    (
    internal val id: Int,
    internal val name: String,
    val username: String,
    val image: String?
)

object RandomPlayers {
    val playersList: List<Player> = listOf(
        Player(1, "Julieta", "JulietaAbuin", "http://someUrl.com"),
        Player(2, "Juan", "JuanGottling", "http://someUrl.com"),
        Player(3, "Guilermo", "GuillermoF", "http://someImage.com")
    )
}