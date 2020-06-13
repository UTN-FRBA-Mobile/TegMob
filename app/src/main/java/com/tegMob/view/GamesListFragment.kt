package com.tegMob.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.tegMob.R
import com.tegMob.connectivity.socket.MatchHandler
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.GamesListViewModel
import io.socket.client.Socket
import kotlinx.android.synthetic.main.games_list_fragment.*

class GamesListFragment : MyFragment() {
    private lateinit var viewModel : GamesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.games_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getPassedData()
        gamesList.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        fetchGames()
        initSearchBar()
    }

    override fun getPassedData() {
        viewModel.imageURI = arguments?.getString("imageURI").toString()
        viewModel.userId = arguments?.getString("userId")!!
        viewModel.userName = arguments?.getString("user").toString()
    }

    override fun initViewModel() {
        viewModel = GamesListViewModel()
        context?.let { viewModel.init(this, listener, it) }    }

    private fun initSearchBar(){
        game_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               //viewModel.search(newText)
                return false
            }
        }
        )
    }

    private fun fetchGames() {
        viewModel.getGames()
        progressBar.visibility = GONE
        gamesList.visibility = VISIBLE
        val handler = Handler()
        val delay: Long = 10000 //milliseconds

        handler.postDelayed(object : Runnable {
            override fun run() {
                viewModel.getGames()
                handler.postDelayed(this, delay)
            }
        }, delay)
    }

   companion object {
        @JvmStatic
        fun newInstance() = GamesListFragment()
    }
}