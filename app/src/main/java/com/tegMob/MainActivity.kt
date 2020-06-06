package com.tegMob

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tegMob.utils.MyFragment
import com.tegMob.view.InitialFragment
import com.tegMob.view.MapFragment

class MainActivity : AppCompatActivity(), MyFragment.OnFragmentInteractionListener {

    private lateinit var fragment : MyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = if (savedInstanceState != null ) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            (supportFragmentManager.findFragmentByTag(TAG_MAP_FRAGMENT) as MapFragment?)!!
        } else {
            InitialFragment()
        }

        supportFragmentManager.popBackStack(
            BACK_STACK_ROOT_TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        );
        //        DESCOMENTO ESTO PARA INICIAR EL PARTIDO SIN LAS PANTALLAS PREVIAS CADA VEZ QUE QUIERO PROBAR (Guille)
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MapFragment.newInstance())
//                    .commitNow()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
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

    override fun showFragment(fragment: Fragment, tag : String) {
        supportFragmentManager.beginTransaction()
            //.addToBackStack(null)
            //.setCustomAnimations(R.anim.fragment_push_enter, R.anim.fragment_push_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }

    companion object {
        private const val BACK_STACK_ROOT_TAG = "root_fragment" //tag activity as root in stack
        private const val TAG_MAP_FRAGMENT = "map_fragment"
    }
}
