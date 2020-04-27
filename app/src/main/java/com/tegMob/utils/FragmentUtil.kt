package com.tegMob.utils

import androidx.fragment.app.*
import com.tegMob.R
import com.tegMob.view.InitialFragment


object FragmentUtil {

    fun loadNextFragment(fragment: Fragment) {
        FragmentActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}