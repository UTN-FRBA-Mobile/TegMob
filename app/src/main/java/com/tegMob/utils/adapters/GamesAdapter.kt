package com.tegMob.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tegMob.R
import com.tegMob.models.Game
import com.tegMob.utils.MyFragment
import kotlinx.android.synthetic.main.list_item_game.view.*
import java.util.*
import kotlin.collections.ArrayList

class GamesAdapter(private val games : List<Game>,
                   private val listener: MyFragment.OnFragmentInteractionListener?):
    RecyclerView.Adapter<GamesAdapter.ViewHolder>(), Filterable

{

    private var filteredGames : List<Game>

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
        holder.gameName.text = game.name
        holder.gameDescription.text = game.description
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameName : TextView = itemView.gameName
        val gameDescription : TextView = itemView.gameDescription
    }

    fun search(newText: String?) = this.filter.filter(newText)

    override fun getFilter() = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val resultGamesList : ArrayList<Game> = ArrayList()

                filteredGames =
                    if (charSearch.isEmpty()) {
                        games
                    } else {
                        for (game in games) {
                            if (charSearch.toLowerCase(Locale.ROOT) in game.name.toLowerCase(Locale.ROOT)) {
                                resultGamesList.add(game)

                            }
                        }
                        resultGamesList.toList()
                    }

                val filterResults = FilterResults()

                filterResults.values = filteredGames

                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredGames = results?.values as List<Game>
                notifyDataSetChanged()
            }

    }

}
