package com.tegMob.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tegMob.R
import com.tegMob.models.Player
import kotlinx.android.synthetic.main.list_item_game.view.*
import kotlinx.android.synthetic.main.list_item_player.view.*

class PlayersAdapter(private var players: List<Player>) : RecyclerView.Adapter<PlayersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_player, parent, false
        )
        return ViewHolder(itemView)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerUserName : TextView = itemView.playerUserName
       // TODO val playerImage : ImageView = itemView.playerImage.setImageURI()
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayersAdapter.ViewHolder, position: Int) {
        val player = players[position]
        holder.playerUserName.text = player.username
        //TODO set image of player
    }

}