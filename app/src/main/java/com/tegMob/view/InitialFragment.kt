package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
        initViewModel()
        emptyFields()
    }

    override fun initViewModel(){
        //viewModel = ViewModelProvider(activity!!).get(LogInViewModel::class.java)
        viewModel = LogInViewModel()
        context?.let { viewModel.init(this, listener, it) }
    }

    private fun initButtons(){
        logInButton.setOnClickListener{viewModel.loginButtonClick()}
        signUpButton.setOnClickListener{viewModel.signUpButtonClick()}
    }

    private fun emptyFields(){
        username.setText("")
        password.setText("")
    }

    override fun completedFields(): Boolean {
        return listOf<EditText>(username, password)
            .map { it.text.toString() }
            .all { it.isNotBlank() }
    }

    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InitialFragment().apply {
              arguments = Bundle()
            }
    }
}