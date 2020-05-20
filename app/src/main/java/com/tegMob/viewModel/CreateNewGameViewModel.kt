package com.tegMob.viewModel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.models.RandomPlayers
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.adapters.PlayersAdapter
import kotlinx.android.synthetic.main.new_game_fragment.*

class CreateNewGameViewModel : MyViewModel() {
    var tableName :String = ""
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
        if (playersAdapter.itemCount < 1) {
            myFragment.progressBar.visibility = View.VISIBLE
        } else {
            myFragment.progressBar.visibility = View.INVISIBLE
            myFragment.startGameButton.visibility = View.VISIBLE
        }

    }

    private fun refreshData() {
        tableName = myFragment.tableName.text.toString()
    }

    fun createNewGame() {
        refreshData()
        if (tableName == "") {
            Toast.makeText(myContext, "Ingrese un nombre de mesa", Toast.LENGTH_LONG).show()
        } else {
            //TODO send creation to server
            myFragment.tableNameTextFinal.visibility = View.VISIBLE
            myFragment.tableNameTextFinal.text = tableName
            hideTableCreation()

            //Get players
            loadDummyPlayersList()
        }
    }

    private fun hideTableCreation() {
        myFragment.worldImage.visibility = View.GONE
        myFragment.createGameButton.visibility = View.GONE
        myFragment.tableName.visibility = View.GONE
        myFragment.tableNameText.visibility = View.INVISIBLE
    }


}