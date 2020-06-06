package com.tegMob.connectivity.socket

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

object MatchHandler {
    private val URL = "http://localhost:8000"
    private var mSocket: Socket? = null

    init {
        onCreate()
    }

    private fun onCreate() {
        try {
            mSocket = IO.socket(URL)
            Log.d("success", "")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket!!.connect()
    }

    private fun getSocketInstance(): Socket? {
        return mSocket
    }

    fun joinMatch() {
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)

        if (mSocket!!.connected()){
            Log.i("INFO SOCKET", "socket is connected")
        } else {
            Log.i("ERROR SOCKET", "not connected")
        }
    }

    var onConnect = Emitter.Listener{ mSocket!!.emit("hello")}


}