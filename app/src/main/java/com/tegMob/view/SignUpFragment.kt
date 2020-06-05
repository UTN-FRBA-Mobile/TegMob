package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tegMob.R
import com.tegMob.utils.MyFragment
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
        initViewModel()
        viewModel.loadData()
        initButtons()
    }

    override fun initViewModel(){
        viewModel = SignUpViewModel()
        context?.let { viewModel.init(this, listener, it) }
    }

    private fun initButtons() {
        signUpButtonFinish.setOnClickListener{ viewModel.signUpFinishButton() }
    }


    override fun onPause() {
        viewModel.saveData()
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

    companion object {
        @JvmStatic
        fun newInstance() =
            SignUpFragment().apply {
                arguments = Bundle()
            }
    }

}