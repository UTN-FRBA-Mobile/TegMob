package com.tegMob.viewModel

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.connectivity.ClientBuilder
import com.tegMob.connectivity.MatchesRouter
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.models.Game
import com.tegMob.models.RandomGames
import com.tegMob.utils.MyViewModel
import com.tegMob.utils.adapters.GamesAdapter
import com.tegMob.view.MapFragment
import kotlinx.android.synthetic.main.games_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GamesListViewModel : MyViewModel() {
    var imageURI = ""
    var userId = 0
    var userName = ""
    private lateinit var gamesAdapter: GamesAdapter
    private val matchesClient = ClientBuilder.MatchesClientBuilder.buildService(MatchesRouter::class.java)


    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

    fun loadDummyGameList() {
        gamesAdapter = GamesAdapter(
            RandomGames.gamesList,
            this
        )
        myFragment.gamesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gamesAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        myFragment.progressBar.visibility = GONE
        myFragment.gamesList.visibility = VISIBLE

    }

    //TODO replace loadDummyGamesList for real on server
    fun getGames(){
        val call = matchesClient.getGamesList()
        gamesAdapter = GamesAdapter(listOf(), this)
        call.enqueue(object : Callback<List<MatchDTOs.MatchListItem>> {
            override fun onResponse(call: Call<List<MatchDTOs.MatchListItem>>, response: Response<List<MatchDTOs.MatchListItem>>) {
                if (response.isSuccessful){
                    myFragment.progressBar.visibility = View.GONE
                    gamesAdapter.games = response.body()!!
                    myFragment.gamesList.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = gamesAdapter
                    }
                }
            }
            override fun onFailure(call: Call<List<MatchDTOs.MatchListItem>>, error: Throwable) {
                Toast.makeText(myContext, "No games founds!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun search(newText: String?)= gamesAdapter.search(newText)

    fun joinMatch(game: MatchDTOs.MatchListItem) {
        val call = matchesClient.addPlayer(game.id, MatchDTOs.MatchPlayerAddDTO(userName))
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                //TODO CHANGE CODE 400 WHEN IT WORKS IN SERVER
                if (response.isSuccessful && response.code() == 200 || response.code() == 400){
                    //TODO MAKE A SOCKET CONNECTION WITH game.socket ATTRIBUTE
                    myFragment.listener!!.showFragment(MapFragment())
                } else {
                    Toast.makeText(myContext, "Hubo un error al unirse a la partida", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Unit>, error: Throwable) {
                Toast.makeText(myContext, "No games founds!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}