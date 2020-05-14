package com.tegMob.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tegMob.R
import com.tegMob.utils.MyFragment

class CreateNewGameFragment : MyFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_game_fragment, container, false)
    }

    override fun getPassedData() {
        TODO("Not yet implemented")
    }

    override fun setDataToPass(): Bundle? {
        TODO("Not yet implemented")
    }

}