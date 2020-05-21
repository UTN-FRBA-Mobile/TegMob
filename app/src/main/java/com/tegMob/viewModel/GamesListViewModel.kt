package com.tegMob.viewModel

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.models.RandomGames
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.adapters.GamesAdapter
import kotlinx.android.synthetic.main.games_list_fragment.*


class GamesListViewModel : MyViewModel() {
    var imageURI = ""
    var userId = 0
    var userName = ""
    private lateinit var gamesAdapter: GamesAdapter


    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

    fun loadDummyGameList(){
        gamesAdapter = GamesAdapter(
            RandomGames.gamesList,
            myListener
        )
        myFragment.gamesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gamesAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }

    fun search(newText: String?)= gamesAdapter.search(newText)

}