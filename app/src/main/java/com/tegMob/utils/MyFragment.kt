package com.tegMob.utils

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

abstract class MyFragment : Fragment() {
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

    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
        fun showFragment(fragment: Fragment, tag : String)
    }

    open fun completedFields(): Boolean {
        return true
    }

    abstract fun getPassedData()
    abstract fun initViewModel()
    open fun getCountryImages(): List<ImageView> = listOf()
    open fun getCountryNumbers(): List<TextView> = listOf()
    open fun getCountryTexts(): List<String> = listOf()
}
