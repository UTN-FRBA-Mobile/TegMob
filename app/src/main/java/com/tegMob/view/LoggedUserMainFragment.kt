package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tegMob.R
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.LoggedUserMainViewModel
import kotlinx.android.synthetic.main.initial_fragment.*
import kotlinx.android.synthetic.main.logged_user_main_fragment.*

class LoggedUserMainFragment : MyFragment() {

    private lateinit var viewModel : LoggedUserMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.logged_user_main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initButtons()
        getPassedData()
    }

    private fun initButtons(){
        newGameButton.setOnClickListener{viewModel.newGameButtonClick()}
        joinGameButton.setOnClickListener{viewModel.joinGameButtonClick()}
    }

    override fun initViewModel() {
        viewModel = LoggedUserMainViewModel()
        context?.let { viewModel.init(this, listener, it) }
        viewModel.username = arguments?.getString("user")?: "PlayerName error"
        viewModel.userId = arguments?.getString("userId") ?: ""

        //TODO get player avatar

        //TODO get player info
    }

    override fun getPassedData() {
        playerName.text = viewModel.username

        //TODO show player avatar
        //TODO show other info ¿?
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InitialFragment().apply {
                arguments = Bundle()
            }
    }
}