package com.tegMob.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tegMob.R
import com.tegMob.view.*

abstract class MyFragment : Fragment(), View.OnClickListener {
    var listener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    //all the apps button clicks behaviour
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.logInButton -> {
                if (completedFields()) {
                    listener?.showFragment(LoggedUserMainFragment())
                } else {
                    Toast.makeText(context, "Please fill empty fields", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.signUpButton -> {
                listener?.showFragment(SignUpFragment())
            }
            R.id.myProfileButton -> {
                //TODO
            }
            R.id.joinGameButton -> {
                listener?.showFragment(GamesListFragment())
            }
            R.id.newGameButton -> {
                listener?.showFragment(CreateNewGameFragment())
            }
            R.id.signUpButtonFinish -> {
                if (completedFields()) {
                    listener?.showFragment(InitialFragment())
                } else {
                    Toast.makeText(context, "Please fill empty fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
    }

    open fun completedFields(): Boolean {
        return true
    }
}
