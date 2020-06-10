package com.tegMob.viewModel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.connectivity.TegMobClient
import com.tegMob.connectivity.routers.MatchesRouter
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.connectivity.socket.MatchHandler
import com.tegMob.connectivity.socket.MatchHandler.getSocket
import com.tegMob.connectivity.socket.MatchHandler.sendMatchInitEvent
import com.tegMob.models.Player
import com.tegMob.models.RandomPlayers
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.adapters.PlayersAdapter
import com.tegMob.view.MapFragment
import io.socket.client.Socket
import kotlinx.android.synthetic.main.new_game_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewGameViewModel : MyViewModel() {
    var tableName: String = ""
    var userName: String = ""
    private var matchPlayersSize: String = ""
    private var playersAdapter: PlayersAdapter = PlayersAdapter(listOf(), this)
    private val matchesClient = TegMobClient.buildService(MatchesRouter::class.java)
    private val TAG_MAP_FRAGMENT = "map_fragment"

    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

    private fun createMatch() {
        val call = matchesClient.createMatch(
            MatchDTOs.MatchCreationDTO(
                name = tableName,
                owner = userName,
                size = matchPlayersSize.toInt()
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
                    myFragment.addPlayerButton.visibility = View.VISIBLE
                    hideTableCreation()
                    //Get players
                    loadDummyPlayersList()
                    //TODO Receive match id after creation from server
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(myContext, "La mesa no se pudo crear", Toast.LENGTH_SHORT).show()
                throw t
            }
        }
        )
    }

    private fun loadDummyPlayersList() {
        playersAdapter = PlayersAdapter(RandomPlayers.playersList, this)
        refreshPlayersList()

        if (playersAdapter.itemCount < matchPlayersSize.toInt() -1) {
            myFragment.progressBar.visibility = View.VISIBLE
        } else {
            myFragment.progressBar.visibility = View.INVISIBLE
            myFragment.startGameButton.visibility = View.VISIBLE
        }

    }

    private fun refreshData() {
        tableName = myFragment.tableName.text.toString()
        matchPlayersSize = myFragment.numberOfPlayers.text.toString()
    }

    private fun completeFields(): Boolean {
        if (tableName == "") {
            Toast.makeText(myContext, "Ingrese un nombre de mesa", Toast.LENGTH_LONG).show()
            return false
        }

        if (matchPlayersSize == "" || matchPlayersSize.toInt() < 2 || matchPlayersSize.toInt() > 6) {
            Toast.makeText(myContext,"Ingrese una cantidad de jugadores entre 2 y 6", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun createNewGame() {
        refreshData()
        if (completeFields()) createMatch()
    }

    private fun hideTableCreation() {
        myFragment.createGameButton.visibility = View.GONE
        myFragment.tableName.visibility = View.GONE
        myFragment.tableNameText.visibility = View.INVISIBLE
        myFragment.numberOfPlayers.visibility = View.INVISIBLE
    }

    private fun getFakePlayerFromServer(): Player {
        return Player(1, "Machine", "Skynet", null)
    }

    fun addNewPlayer() {
        if (playersAdapter.players.size < matchPlayersSize.toInt() - 1) {
            playersAdapter.players = playersAdapter.players.plus(getFakePlayerFromServer())
            refreshPlayersList()
        } else {
            Toast.makeText(myContext, "El máximo es de ${matchPlayersSize.toInt()} jugadores para esta mesa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshPlayersList() {
        myFragment.playersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playersAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    fun removePlayerFromMatch(username: String) {
        val call = matchesClient.removePlayer(tableName, MatchDTOs.MatchPlayerRemoveDTO(userName))
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                //TODO REMOVE 400 WHEN ENDPOINT WORKS OK
                if (response.isSuccessful && response.code() == 200 || response.code() == 400) {
                    playersAdapter.players = playersAdapter.players.filter { it.username != username }
                    refreshPlayersList()
                } else {
                    Toast.makeText(myContext,"No se pudo eliminar al usuario de la partida", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(myContext,"No se pudo eliminar al usuario de la partida", Toast.LENGTH_SHORT).show()
                throw t
            }
        }
        )
    }

    fun startNewGame() {
        MatchHandler.startMatch()
        getSocket()!!.on(Socket.EVENT_CONNECT, sendMatchInitEvent(tableName))
        myListener?.showFragment(MapFragment(), TAG_MAP_FRAGMENT)
    }
}