package com.tegMob

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.tegMob.connectivity.dtos.MatchDTOs
import com.tegMob.utils.MyFragment
import com.tegMob.view.CreateNewGameFragment
import com.tegMob.view.InitialFragment
import com.tegMob.view.MapFragment
import com.tegMob.view.WaitingFragment
import kotlinx.coroutines.delay
import java.sql.Time

class MainActivity : AppCompatActivity(), MyFragment.OnFragmentInteractionListener {

    private lateinit var fragment: MyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            (supportFragmentManager.findFragmentByTag(TAG_MAP_FRAGMENT) as MapFragment?)!!
        } else {
            InitialFragment()
        }

        Log.i("try_attack",
            Gson().toJson(
                MatchDTOs.MatchTryAttack(
                attacker = "attackerCountry!!",
                defender = "defenderCountry!!",
                id_match = "matchId"
            )))

        supportFragmentManager.popBackStack(
            BACK_STACK_ROOT_TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        );
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            //.addToBackStack(BACK_STACK_ROOT_TAG)
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

    override fun showFragment(fragment: Fragment, tag: String) {
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

    //    override fun onWindowFocusChanged(hasFocus: Boolean) {
    //        super.onWindowFocusChanged(hasFocus)
    //        if (hasFocus) hideSystemUI()
    //    }

    fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }



}
