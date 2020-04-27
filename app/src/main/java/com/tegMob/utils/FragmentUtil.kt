package com.tegMob.utils

import androidx.fragment.app.*
import com.tegMob.R
import com.tegMob.view.InitialFragment


object FragmentUtil {
    private const val BACK_STACK_ROOT_TAG = "root_fragment"

    fun loadFirstFragment(activity: FragmentActivity){
        activity.supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        activity.supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, InitialFragment())
            .addToBackStack(BACK_STACK_ROOT_TAG)
            .commit()
    }

    fun loadNextFragment(fragment: Fragment, activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}