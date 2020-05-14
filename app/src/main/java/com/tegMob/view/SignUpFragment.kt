package com.tegMob.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tegMob.R
import com.tegMob.utils.MyFragment
import kotlinx.android.synthetic.main.initial_fragment.*
import com.tegMob.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.sign_up_fragment.*

class SignUpFragment : MyFragment() {
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(SignUpViewModel::class.java)

        firstName.setText(viewModel.firstName)
        lastName.setText(viewModel.lastName)
        email.setText(viewModel.email)
        usernameSignUp.setText(viewModel.userNameSignUp)

        initButtons()
    }

    private fun initButtons() {
        signUpButtonFinish.setOnClickListener(this)
    }


    override fun onPause() {
        viewModel.firstName = firstName?.text.toString()
        viewModel.lastName = lastName?.text.toString()
        viewModel.email = email?.text.toString()
        viewModel.userNameSignUp = usernameSignUp?.text.toString()

        super.onPause()
    }

    override fun completedFields(): Boolean {
        return listOf<EditText>(usernameSignUp, passwordSignUp, firstName, lastName, email)
            .map { it.text.toString() }
            .all { it.isNotBlank() }
    }

    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    override fun setDataToPass(): Bundle? {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SignUpFragment().apply {
                arguments = Bundle()
            }
    }

}