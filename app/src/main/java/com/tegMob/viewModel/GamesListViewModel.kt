package com.tegMob.viewModel

import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.connectivity.TegMobClient
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.connectivity.routers.MatchesRouter
import com.tegMob.connectivity.socket.MatchHandler
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.adapters.GamesAdapter
import com.tegMob.view.MapFragment
import com.tegMob.view.WaitingFragment
import kotlinx.android.synthetic.main.games_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GamesListViewModel : MyViewModel() {
    var imageURI = ""
    var userId = ""
    var userName = ""
    private var gamesAdapter: GamesAdapter = GamesAdapter(listOf(), this)
    private val matchesClient = TegMobClient.buildService(MatchesRouter::class.java)
    private var matchId: String = ""

    /*fun loadDummyGameList() {
        gamesAdapter = GamesAdapter(
            RandomGames.GAMES_LIST_DTO,
            this
        )
        myFragment.gamesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gamesAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        myFragment.progressBar.visibility = GONE
        myFragment.gamesList.visibility = VISIBLE

    }*/

    fun getGames(){
        val call = matchesClient.getGamesList()
        gamesAdapter = GamesAdapter(listOf(), this)
        call.enqueue(object : Callback<List<MatchDTOs.MatchListItemDTO>> {
            override fun onResponse(call: Call<List<MatchDTOs.MatchListItemDTO>>, response: Response<List<MatchDTOs.MatchListItemDTO>>) {
                if (response.isSuccessful && response.code() == 200){
                    checkForNewGamesAndAddThem(response)
                } else {
                    Toast.makeText(myContext, "No games found", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<MatchDTOs.MatchListItemDTO>>, error: Throwable) {
                Toast.makeText(myContext, "No games found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkForNewGamesAndAddThem(response: Response<List<MatchDTOs.MatchListItemDTO>>) {
        val gamesToAdd = response.body()!!.filter { g ->
            g.stage == "CREATED" && !gamesAdapter.games.map { it.id }.contains(g.id)
        }
        gamesToAdd.forEach { game ->
            gamesAdapter.filteredGames = gamesAdapter.filteredGames.plus(game)
            gamesAdapter.games = gamesAdapter.filteredGames
            if (myFragment.isVisible)
            myFragment.gamesList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = gamesAdapter
            }
        }
    }


    fun search(newText: String?)= gamesAdapter.search(newText)

    override fun setDataToPass(): Bundle = bundleOf(
        "matchId" to matchId,
        "userId" to userId
    )

    fun joinMatch(game: MatchDTOs.MatchListItemDTO) {
        val call = matchesClient.addPlayer(game.id, MatchDTOs.MatchPlayerAddDTO(userId))
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful && response.code() == 200){
                    MatchHandler.connectToServerAndDoHandShake(userId)
                    matchId = game.id
                    buttonClick(WaitingFragment())
                } else {
                    Toast.makeText(myContext, "Hubo un error al intentar unirse a la partida", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Unit>, error: Throwable) {
                Toast.makeText(myContext, "No games founds!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}