package com.tegMob.connectivity.socket

import android.util.Log
import com.tegMob.viewModel.CreateNewGameViewModel
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

object MatchHandler {
    private val URL = "http://tegmobapp.eba-2psmvpsn.us-east-1.elasticbeanstalk.com:8000/"
    private var mSocket: Socket? = null

    private fun connect() {
        try {
            mSocket = IO.socket(URL)
            mSocket!!.connect()
            Log.d("SUCCESS CONNECTION TO SERVER SOCKET", "")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }
    }

    fun getSocket(): Socket? {
        return mSocket
    }

    fun startMatch() {
        connect()
    }

    fun sendMatchInitEvent(matchId: String): Emitter.Listener? {
       return Emitter.Listener { mSocket!!.emit("match_init", matchId) }
    }


}