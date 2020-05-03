package com.tegMob.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.tegMob.R
import com.tegMob.view.GamesListFragment
import com.tegMob.view.InitialFragment
import com.tegMob.view.LoggedUserMainFragment
import com.tegMob.view.SignUpFragment

abstract class MyFragment : Fragment(), View.OnClickListener{
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
        when(view!!.id){
            R.id.logInButton->{
                listener?.showFragment(LoggedUserMainFragment())
            }
            R.id.signUpButton->{
                listener?.showFragment(SignUpFragment())
            }
            R.id.myProfileButton->{
                //TODO
            }
            R.id.joinGameButton->{
                listener?.showFragment(GamesListFragment())
            }
            R.id.newGameButton->{
                //TODO
            }
            R.id.signUpButtonFinish ->{
                listener?.showFragment(InitialFragment())
            }
        }
    }

    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
    }

}