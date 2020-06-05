package com.tegMob.utils

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tegMob.R
import com.tegMob.view.*

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

}
