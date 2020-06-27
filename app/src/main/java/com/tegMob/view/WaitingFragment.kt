package com.tegMob.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.tegMob.R
import com.tegMob.connectivity.socket.MatchHandler
import com.tegMob.utils.MyFragment
import io.socket.emitter.Emitter

class WaitingFragment : MyFragment() {
    private val TAG_MAP_FRAGMENT = "map_fragment"
    private var matchId: String = ""
    private var initMapData: String = ""
    private var userId: String = ""
    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.waiting_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPassedData()
        eventSubscriptions()
    }

    private fun eventSubscriptions() {
        MatchHandler.getSocket()!!.on("MATCH_START", initMapFragment)
    }

    private fun setDataToPass() {
        bundle.putString("matchId", matchId)
        bundle.putString("initMapData", initMapData)
        bundle.putString("userId", userId)
    }

    private val initMapFragment = Emitter.Listener {
        //TODO test this
        val map = it[0].toString()
        initMapData = map
        setDataToPass()
        listener!!.showFragment(MapFragment.newInstance(bundle), TAG_MAP_FRAGMENT)
    }

    override fun getPassedData() {
        matchId = arguments?.getString("matchId").toString()
        userId = arguments?.getString("userId").toString()
    }

    override fun initViewModel() {
        TODO("Not yet implemented")
    }



}