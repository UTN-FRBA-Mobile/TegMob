package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tegMob.R
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.CreateNewGameViewModel
import kotlinx.android.synthetic.main.list_item_player.*
import kotlinx.android.synthetic.main.new_game_fragment.*

class CreateNewGameFragment : MyFragment() {
    private lateinit var viewModel: CreateNewGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        initViewModel()
    }

    private fun initButtons() {
        createGameButton.setOnClickListener { viewModel.createNewGame() }
        addPlayerButton.setOnClickListener { viewModel.addNewPlayer() }
        startGameButton.setOnClickListener { viewModel.startNewGame() }
    }

    override fun getPassedData() {
        TODO("Not yet implemented")
    }


    override fun initViewModel() {
        viewModel = CreateNewGameViewModel()
        context?.let { viewModel.init(this, listener, it) }
        viewModel.userName = arguments?.getString("user")?: "PlayerName error"
        viewModel.userId = arguments?.getString("userId")?: ""
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNewGameFragment().apply {
                arguments = Bundle()
            }
    }

}