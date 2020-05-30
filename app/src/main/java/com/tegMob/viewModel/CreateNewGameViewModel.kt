package com.tegMob.viewModel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.connectivity.ClientBuilder
import com.tegMob.connectivity.MatchesRouter
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.models.Player
import com.tegMob.models.RandomPlayers
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.adapters.PlayersAdapter
import com.tegMob.view.MapFragment
import kotlinx.android.synthetic.main.new_game_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewGameViewModel : MyViewModel() {
    var tableName: String = ""
    var userName: String = ""
    private lateinit var playersAdapter: PlayersAdapter

    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

    private fun createMatch() {
        val matchesClient =
            ClientBuilder.MatchesClientBuilder.buildService(
                MatchesRouter::class.java)
        val call = matchesClient.createMatch(
            MatchDTOs.MatchCreationDTO(
                name = tableName,
                owner = userName,
                size = 2
            )
        )
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.isSuccessful) {
                    myFragment.tableNameTextFinal.visibility = View.VISIBLE
                    myFragment.tableNameTextFinal.text = tableName
                    hideTableCreation()
                    //Get players
                    loadDummyPlayersList()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                print("Match couldn't be created. Exception $t")
                Toast.makeText(myContext, "Table couldn't be created", Toast.LENGTH_SHORT).show()
                throw t
            }
        }
        )
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
            createMatch()
        }
    }

    private fun hideTableCreation() {
        myFragment.createGameButton.visibility = View.GONE
        myFragment.tableName.visibility = View.GONE
        myFragment.tableNameText.visibility = View.INVISIBLE
    }

    private fun getFakePlayerFromServer(): Player {
        return Player(1, "Machine", "Skynet", null)
    }

    fun addNewPlayer() {
        if (playersAdapter.players.size < 5) {
            playersAdapter = PlayersAdapter(playersAdapter.players.plus(getFakePlayerFromServer()))
            myFragment.playersList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = playersAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        } else {
            Toast.makeText(myContext, "El máximo es de 6 jugadores", Toast.LENGTH_SHORT).show()
        }
    }

    fun startNewGame() {
        myListener?.showFragment(MapFragment())
    }
}