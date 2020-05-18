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
        Player(1, "Julieta", "JulietaAbuin", "https://i.pinimg.com/originals/c6/ed/d1/c6edd154f50416dba0def70b87777253.jpg"),
        Player(2, "Juan", "JuanGottling", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQVzVT2_U9dF2824iAJk6PRZi5ED5dP3bjzzq07_olJZLAUjcO7&usqp=CAU"),
        Player(3, "Guillermo", "GuillermoF", null)
    )
}