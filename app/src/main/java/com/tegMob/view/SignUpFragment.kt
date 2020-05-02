package com.tegMob.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tegMob.R
import com.tegMob.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.sign_up_fragment.*

class SignUpFragment : Fragment() {
    private var listener:  OnFragmentInteractionListener? = null
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
        username.setText(viewModel.userName)

        signUpButton.setOnClickListener{
            listener!!.signUpFinalized()
        }
    }

    override fun onPause() {
        viewModel.firstName = firstName?.text.toString()
        viewModel.lastName = lastName?.text.toString()
        viewModel.email = email?.text.toString()
        viewModel.userName = username?.text.toString()

        super.onPause()
    }

    interface OnFragmentInteractionListener {
        fun signUpFinalized()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

   companion object {
        @JvmStatic
        fun newInstance() =
            SignUpFragment().apply {
                arguments = Bundle()
            }
    }
}