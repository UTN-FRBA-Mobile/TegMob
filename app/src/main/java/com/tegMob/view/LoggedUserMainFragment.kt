package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tegMob.R
import com.tegMob.utils.MyFragment
import kotlinx.android.synthetic.main.logged_user_main_fragment.*

class LoggedUserMainFragment : MyFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.logged_user_main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons(){
        myProfileButton.setOnClickListener(this)
        newGameButton.setOnClickListener(this)
        joinGameButton.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InitialFragment().apply {
                arguments = Bundle()
            }
    }
}