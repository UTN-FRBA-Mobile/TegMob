package com.tegMob

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tegMob.utils.FragmentUtil
import com.tegMob.view.InitialFragment
import com.tegMob.view.SignUpFragment

class MainActivity : AppCompatActivity(), InitialFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener {
    private lateinit var initialFragment: Fragment
    private lateinit var signUpFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialFragment = InitialFragment()
        FragmentUtil.loadFirstFragment(initialFragment, this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onSignUpChoicePressed() {
        signUpFragment = SignUpFragment()
        //supportFragmentManager.beginTransaction().add(R.id.fragment_container, signUpFragment).addToBackStack(null).commit()
        FragmentUtil.loadNextFragment(signUpFragment, this)
        }

    override fun signUpFinalized() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction().show(initialFragment).commitNow()
    }

}
