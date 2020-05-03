package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.R
import com.tegMob.adapters.GamesAdapter
import com.tegMob.models.RandomGames
import com.tegMob.utils.MyFragment
import kotlinx.android.synthetic.main.games_list_fragment.*

class GamesListFragment : MyFragment() {
    private lateinit var gamesAdapter: GamesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.games_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamesAdapter = GamesAdapter(RandomGames.gamesList, listener)
        gamesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gamesAdapter
        }
    }

   companion object {
        @JvmStatic
        fun newInstance() = GamesListFragment()
    }
}