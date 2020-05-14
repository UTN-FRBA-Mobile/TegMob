package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegMob.R
import com.tegMob.models.Game
import com.tegMob.utils.adapters.GamesAdapter
import com.tegMob.models.RandomGames
import com.tegMob.utils.MyFragment
import com.tegMob.utils.connectivity.Router
import com.tegMob.utils.connectivity.TegService
import kotlinx.android.synthetic.main.games_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesListFragment : MyFragment() {
    private lateinit var gamesAdapter: GamesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.games_list_fragment, container, false)
    }

    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    override fun setDataToPass(): Bundle? {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamesAdapter = GamesAdapter(
            RandomGames.gamesList,
            listener
        )
        gamesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gamesAdapter
        }
    }

//    override fun onStart(){
//        super.onStart()
//        gamesList.visibility = View.GONE
//        progressBar.visibility = View.VISIBLE
//
//        val request = TegService.buildService(Router::class.java)
//        val call = request.getGamesList()
//
//        call.enqueue(object : Callback<List<Game>> {
//            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
//                if (response.isSuccessful){
//                    progressBar.visibility = View.GONE
//                    gamesAdapter = GamesAdapter(response.body()!!, listener)
//                    gamesList.apply {
//                        layoutManager = LinearLayoutManager(context)
//                        adapter = gamesAdapter
//                    }
//                }
//            }
//            override fun onFailure(call: Call<List<Game>>, error: Throwable) {
//                Toast.makeText(activity, "No games founds!", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

   companion object {
        @JvmStatic
        fun newInstance() = GamesListFragment()
    }
}