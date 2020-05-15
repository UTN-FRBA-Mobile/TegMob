package com.tegMob.utils

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel

abstract class MyViewModel () : ViewModel() {
    internal var myListener : MyFragment.OnFragmentInteractionListener? = null
    internal var myContext : Context? = null
    internal lateinit var myFragment : MyFragment

    fun init(fragment : MyFragment, listener: MyFragment.OnFragmentInteractionListener?, context: Context){
        myListener = listener
        myContext = context
        myFragment = fragment
    }

    internal fun buttonClick(fragment : MyFragment){
        fragment.arguments = setDataToPass()
        myListener?.showFragment(fragment)
    }

    abstract fun setDataToPass(): Bundle

}