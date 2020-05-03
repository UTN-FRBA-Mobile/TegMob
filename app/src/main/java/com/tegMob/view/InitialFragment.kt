package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tegMob.R
import com.tegMob.utils.MyFragment
import kotlinx.android.synthetic.main.initial_fragment.*


class InitialFragment : MyFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                return inflater.inflate(R.layout.initial_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons(){
        logInButton.setOnClickListener(this)
        signUpButton.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InitialFragment().apply {
                arguments = Bundle()
            }
    }
}