package com.tegMob

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tegMob.utils.MyFragment
import com.tegMob.view.InitialFragment
import com.tegMob.view.SignUpFragment

class MainActivity : AppCompatActivity(), MyFragment.OnFragmentInteractionListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, InitialFragment())
            .addToBackStack(BACK_STACK_ROOT_TAG)
            .commit()
    }

    //fun for closing app after tapping back twice
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_LONG).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
 //           .setCustomAnimations(R.anim.fragment_push_enter, R.anim.fragment_push_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    companion object {
        private const val BACK_STACK_ROOT_TAG = "root_fragment" //tag activity as root in stack
    }
}
