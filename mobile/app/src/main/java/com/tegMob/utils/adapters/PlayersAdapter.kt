package com.tegMob.utils.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tegMob.R
import com.tegMob.models.Player
import com.tegMob.viewModel.CreateNewGameViewModel
import kotlinx.android.synthetic.main.list_item_player.view.*


class PlayersAdapter(var players: List<Player>, val viewModel: CreateNewGameViewModel): RecyclerView.Adapter<PlayersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_player, parent, false
        )
        return ViewHolder(itemView)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerUserName : TextView = itemView.playerUserName
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayersAdapter.ViewHolder, position: Int) {
        val player = players[position]
        holder.playerUserName.text = player.username
        if (player.image != null) {
            Picasso.get().load(Uri.parse(player.image)).into(holder.itemView.playerImage)
        }
        holder.itemView.deletePlayer.setOnClickListener {viewModel.removePlayerFromMatch(holder.itemView.playerUserName.text as String)}
    }

}