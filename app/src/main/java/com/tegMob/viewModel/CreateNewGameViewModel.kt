package com.tegMob.viewModel

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.models.RandomPlayers
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.adapters.PlayersAdapter
import kotlinx.android.synthetic.main.new_game_fragment.*

class CreateNewGameViewModel : MyViewModel() {
    private lateinit var playersAdapter: PlayersAdapter
    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

    private fun loadDummyPlayersList() {
        playersAdapter = PlayersAdapter(RandomPlayers.playersList)
        myFragment.playersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playersAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        if (playersAdapter.itemCount < 6) {
            myFragment.progressBar.visibility = View.VISIBLE
        } else {
            myFragment.progressBar.visibility = View.INVISIBLE
            myFragment.startGameButton.visibility = View.VISIBLE
        }

    }

    fun createNewGame() {
        //TODO send creation to server
        //Get players
        loadDummyPlayersList()
    }


}