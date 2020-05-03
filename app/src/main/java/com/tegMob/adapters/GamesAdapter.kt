package com.tegMob.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tegMob.R
import com.tegMob.models.Game
import com.tegMob.utils.MyFragment
import kotlinx.android.synthetic.main.list_item_game.view.*

class GamesAdapter(private val games : List<Game>, private val listener: MyFragment.OnFragmentInteractionListener?):
    RecyclerView.Adapter<GamesAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_game, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount() = games.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]
        holder.gameName.text = game.name
        holder.gameDescription.text = game.description
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameName : TextView = itemView.gameName
        val gameDescription : TextView = itemView.gameDescription
    }
}
