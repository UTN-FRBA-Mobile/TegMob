package com.tegMob.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.tegMob.R
import com.tegMob.connectivity.ClientBuilder
import com.tegMob.connectivity.MatchesRouter
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.GamesListViewModel
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
        viewModel.loadDummyGameList()
        initSearchBar()
    }

    override fun getPassedData() {
        viewModel.imageURI = arguments?.getString("imageURI").toString()
        viewModel.userId = arguments?.getInt("userId")!!
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
                viewModel.search(newText)
                return false
            }
        }
        )
    }

    /*override fun onStart() {
        super.onStart()

        val handler = Handler()
        val delay: Long = 1000 //milliseconds

        handler.postDelayed(object : Runnable {
            override fun run() {
                viewModel.loadDummyGameList()
                handler.postDelayed(this, delay)
            }
        }, delay)
    }*/

   companion object {
        @JvmStatic
        fun newInstance() = GamesListFragment()
    }
}