package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.tegMob.R
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

//    override fun onStart(){
//        super.onStart()
        //viewModel.
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