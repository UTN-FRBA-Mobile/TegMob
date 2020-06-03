package com.tegMob.viewModel

import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.games_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GamesListViewModel : MyViewModel() {
    var imageURI = ""
    var userId = 0
    var userName = ""
    private lateinit var gamesAdapter: GamesAdapter


    override fun setDataToPass(): Bundle {
        TODO("Not yet implemented")
    }

//    fun loadDummyGameList(){
//        gamesAdapter = GamesAdapter(
//            RandomGames.gamesList,
//            myListener
//        )
//        myFragment.gamesList.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = gamesAdapter
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        }
//
//    }

    fun getGames(){
        val request = ClientBuilder.MatchesClientBuilder.buildService(MatchesRouter::class.java)
        val call = request.getGamesList()
        call.enqueue(object : Callback<List<MatchDTOs.MatchListItem>> {
            override fun onResponse(call: Call<List<MatchDTOs.MatchListItem>>, response: Response<List<MatchDTOs.MatchListItem>>) {
                if (response.isSuccessful){
                    myFragment.progressBar.visibility = View.GONE
                    gamesAdapter = GamesAdapter(response.body()!!, myListener)
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

}