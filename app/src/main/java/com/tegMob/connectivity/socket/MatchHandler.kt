package com.tegMob.connectivity.socket

import android.content.Context
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object MatchHandler {
    private val URL = "http://localhost:8000"
    private var iSocket: Socket? = null

    init {
        onCreate()
    }

    private fun onCreate() {
        try {
            val opts: IO.Options = IO.Options()
            //opts.query = "auth_token=$authToken"
            iSocket = IO.socket(URL, opts)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    private fun getSocketInstance(): Socket? {
        return iSocket
    }

    fun joinMatch(context: Context?) {
        this.getSocketInstance()!!.connect()
        if (iSocket!!.connected()){
            Toast.makeText(context,"Socket Connected!!",Toast.LENGTH_SHORT).show();
        }
    }

}