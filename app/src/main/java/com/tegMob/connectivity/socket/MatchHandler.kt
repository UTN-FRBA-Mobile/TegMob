package com.tegMob.connectivity.socket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.lang.Appendable

object MatchHandler {
    private val URL = "http://192.168.1.23:4000/"
    private var mSocket: Socket? = null
    private var matchId = ""

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

    fun iam(userId: String) = Emitter.Listener { mSocket!!.emit("IAM", userId) }

    fun connectToServerAndDoHandShake(userId: String) {
        connect()
        mSocket!!.on("WHORU", iam(userId))
    }

    fun startMatch(matchId: String) {
        mSocket!!.emit("MATCH_INIT", matchId)
    }

    fun tryAttack(attackerCountry: String, defenderCountry: String, matchId: String) {
        mSocket!!.emit("TRY_ATTACK", attackerCountry, defenderCountry, matchId)
    }

}