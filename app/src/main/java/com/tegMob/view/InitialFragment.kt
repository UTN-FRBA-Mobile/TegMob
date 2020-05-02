package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tegMob.R
import com.tegMob.utils.FragmentUtil
import kotlinx.android.synthetic.main.initial_fragment.*


class InitialFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                return inflater.inflate(R.layout.initial_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logInButton.setOnClickListener {
            //ir al server a chequear si existe o mockear que siempre se loguee bien
            //ir a pantalla principal del juego user logueado
        }
        signUpButton.setOnClickListener {
            onSignUpPressed()
        }
    }

    private fun onSignUpPressed() {

        FragmentUtil.loadNextFragment(SignUpFragment(), activity!!)
//        activity!!.supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, SignInFragment())
//            .commit()
    }

    interface OnFragmentInteractionListener {
    fun showFragment(fragment: Fragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Title.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance() =
            InitialFragment().apply {
                arguments = Bundle()
            }
    }
}