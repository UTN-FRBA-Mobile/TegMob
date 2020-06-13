package com.tegMob.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tegMob.R
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.viewModel.GamesListViewModel
import kotlinx.android.synthetic.main.list_item_game.view.*
import java.util.*
import kotlin.collections.ArrayList

class GamesAdapter(var games: List<MatchDTOs.MatchListItemDTO>, val viewModel: GamesListViewModel) :
    RecyclerView.Adapter<GamesAdapter.ViewHolder>(), Filterable {

    private var filteredGames: List<MatchDTOs.MatchListItemDTO>

    init {
        filteredGames = games
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_game, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount() = filteredGames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = filteredGames[position]

        holder.bind(game)
        holder.itemView.setOnClickListener { viewModel.joinMatch(game) }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameName: TextView = itemView.gameName
        private val gameDescription: TextView = itemView.gameDescription

        fun bind(game: MatchDTOs.MatchListItemDTO) {
            gameName.text = game.matchname
        }
    }

    fun search(newText: String?) = this.filter.filter(newText)

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charSearch = constraint.toString()
            val resultGamesListDTO: ArrayList<MatchDTOs.MatchListItemDTO> = ArrayList()

            filteredGames =
                if (charSearch.isEmpty()) {
                    games
                } else {
                    for (game in games) {
                        if (charSearch.toLowerCase(Locale.ROOT) in game.matchname.toLowerCase(Locale.ROOT)) {
                            resultGamesListDTO.add(game)

                        }
                    }
                    resultGamesListDTO.toList()
                }

            val filterResults = FilterResults()

            filterResults.values = filteredGames

            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredGames = results?.values as List<MatchDTOs.MatchListItemDTO>
            notifyDataSetChanged()
        }

    }

}
