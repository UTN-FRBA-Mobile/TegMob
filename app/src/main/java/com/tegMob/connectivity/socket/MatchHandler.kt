package com.tegMob.connectivity.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

object MatchHandler {
    private val URL = "http://192.168.1.110:4000/"
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

    fun iam(userId: String) = Emitter.Listener { mSocket!!.emit("IAM", EventIAMData(userId)) }

    fun connectToServerAndDoHandShake(userId: String) {
        connect()
        mSocket!!.on("WHORU", iam(userId))
    }

    fun startMatch(matchId: String): Emitter.Listener? {
       return Emitter.Listener { mSocket!!.emit("MATCH_INIT", matchId) }
    }

    data class EventIAMData(val userid: String)

}