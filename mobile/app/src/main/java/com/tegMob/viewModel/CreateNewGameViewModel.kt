package com.tegMob.viewModel

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.connectivity.TegMobClient
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.connectivity.dtos.UserDTOs
import com.tegMob.connectivity.routers.MatchesRouter
import com.tegMob.connectivity.routers.UsersRouter
import com.tegMob.connectivity.socket.MatchHandler
import com.tegMob.models.Player
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.adapters.PlayersAdapter
import com.tegMob.view.MapFragment
import com.tegMob.view.WaitingFragment
import kotlinx.android.synthetic.main.new_game_fragment.*
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewGameViewModel : MyViewModel() {
    var tableName: String = ""
    var userName: String = ""
    var userId: String = ""
    private var matchPlayersSize: String = ""
    private var matchId: String = ""
    private var playersAdapter: PlayersAdapter = PlayersAdapter(listOf(), this)
    private val matchesClient = TegMobClient.buildService(MatchesRouter::class.java)
    private val usersClient = TegMobClient.buildService(UsersRouter::class.java)

    override fun setDataToPass(): Bundle = bundleOf(
            "matchId" to matchId,
            "userId" to userId
        )

    private fun createMatch() {
        val call = matchesClient.createMatch(
            MatchDTOs.MatchCreationDTO(
                matchname = tableName,
                owner = userId,
                size = matchPlayersSize.toInt()
            )
        )
        call.enqueue(object : Callback<MatchDTOs.MatchCreationResponseDTO> {
            override fun onResponse(
                call: Call<MatchDTOs.MatchCreationResponseDTO>,
                response: Response<MatchDTOs.MatchCreationResponseDTO>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    myFragment.tableNameTextFinal.visibility = View.VISIBLE
                    myFragment.tableNameTextFinal.text = tableName
                    matchId = response.body()!!.id
                    setDataToPass()
                    hideTableCreation()
                    //Get players
                    startPlayersPoll()
                } else {
                    Toast.makeText(myContext, "La mesa no se pudo crear", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MatchDTOs.MatchCreationResponseDTO>, t: Throwable) {
                Toast.makeText(myContext, "La mesa no se pudo crear", Toast.LENGTH_SHORT).show()
                throw t
            }
        }
        )
    }

    private fun startPlayersPoll() {
        playersAdapter = PlayersAdapter(listOf(), this)
        refreshPlayersList()
        fetchPlayersFromServer()
        val handler = Handler()
        val delay: Long = 5000 //milliseconds

        handler.postDelayed(object : Runnable {
            override fun run() {
                fetchPlayersFromServer()
                handler.postDelayed(this, delay)
            }
        }, delay)
    }

    private fun fetchPlayersFromServer() {
        matchesClient.getMatchById(matchId)
            .enqueue(object : Callback<MatchDTOs.MatchListItemDTO> {
                override fun onResponse(
                    call: Call<MatchDTOs.MatchListItemDTO>,
                    response: Response<MatchDTOs.MatchListItemDTO>
                ) {
                    if (response.isSuccessful && response.code() == 200) {
                        getPlayers(response.body()!!.players)
                    } else {
                        Toast.makeText(
                            myContext,
                            "No se pudo obtener a los jugadores en este momento",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<MatchDTOs.MatchListItemDTO>, t: Throwable) {
                    Toast.makeText(
                        myContext,
                        "No se pudo obtener a los jugadores en este momento",
                        Toast.LENGTH_SHORT
                    ).show()
                    throw t
                }
            }
            )
    }

    private fun getPlayers(players: List<MatchDTOs.MatchPlayerDTO>) {
        players.filter { p -> !playersAdapter.players.map { it.id }.contains(p.user) }
            .forEach { player ->
                val call = usersClient.getUserById(player.user)
                call.enqueue(object : Callback<UserDTOs.UserResponseDTO> {
                    override fun onResponse(
                        call: Call<UserDTOs.UserResponseDTO>,
                        response: Response<UserDTOs.UserResponseDTO>
                    ) {
                        if (response.isSuccessful && response.code() == 200) {
                            addNewPlayer(
                                Player(
                                    id = player.user,
                                    username = response.body()!!.username,
                                    name = ""
                                )
                            )
                        } else {
                            Toast.makeText(
                                myContext,
                                "No se pudo actualizar la lista de jugadores",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserDTOs.UserResponseDTO>, t: Throwable) {
                        Toast.makeText(
                            myContext,
                            "No se pudo actualizar la lista de jugadores",
                            Toast.LENGTH_SHORT
                        ).show()
                        throw t
                    }
                }
                )
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
            Toast.makeText(
                myContext,
                "Ingrese una cantidad de jugadores entre 2 y 6",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    fun createNewGame() {
        refreshData()
        if (completeFields()) createMatch()
        MatchHandler.connectToServerAndDoHandShake(userId)
    }

    private fun hideTableCreation() {
        myFragment.createGameButton.visibility = View.GONE
        myFragment.tableName.visibility = View.GONE
        myFragment.tableNameText.visibility = View.INVISIBLE
        myFragment.numberOfPlayers.visibility = View.INVISIBLE
    }

    fun addNewPlayer(player: Player) {
        if (playersAdapter.players.size <= matchPlayersSize.toInt() - 1) {
            playersAdapter.players =
                playersAdapter.players.plus(player)
            refreshPlayersList()
        } else {
            Toast.makeText(
                myContext,
                "El máximo es de ${matchPlayersSize.toInt()} jugadores para esta mesa",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun refreshPlayersList() {
        myFragment.playersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playersAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        if (playersAdapter.itemCount <= matchPlayersSize.toInt() - 1) {
            myFragment.progressBar.visibility = View.VISIBLE
        } else {
            myFragment.progressBar.visibility = View.INVISIBLE
            myFragment.startGameButton.visibility = View.VISIBLE
        }
    }

    fun removePlayerFromMatch(username: String) {
        val call = matchesClient.removePlayer(tableName, MatchDTOs.MatchPlayerRemoveDTO(userName))
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    playersAdapter.players =
                        playersAdapter.players.filter { it.username != username }
                    refreshPlayersList()
                } else {
                    Toast.makeText(
                        myContext,
                        "No se pudo eliminar al usuario de la partida",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(
                    myContext,
                    "No se pudo eliminar al usuario de la partida",
                    Toast.LENGTH_SHORT
                ).show()
                throw t
            }
        }
        )
    }

    fun startNewGame() {
        MatchHandler.startMatch(matchId)
        buttonClick(WaitingFragment())
    }
}