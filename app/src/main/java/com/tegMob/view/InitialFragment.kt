package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tegMob.R
import com.tegMob.utils.MyFragment
import com.tegMob.viewModel.LogInViewModel
import kotlinx.android.synthetic.main.initial_fragment.*


class InitialFragment : MyFragment() {
    private lateinit var viewModel: LogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                return inflater.inflate(R.layout.initial_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        viewModel = ViewModelProvider(activity!!).get(LogInViewModel::class.java)
        username.setText(viewModel.username)
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