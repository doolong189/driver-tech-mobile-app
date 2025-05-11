package com.hoanglong180903.driver.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class SocketIOManager() {
    private var socket: Socket? = null
    fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                socket = IO.socket("${Contacts.SOCKET_URL}")
                socket?.connect()
                Log.e(Contacts.TAG, "socket connect")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun join(message : String){
        CoroutineScope(Dispatchers.IO).launch {
            socket?.emit("join", message)
        }
    }

    fun userJoinedTheChat(){
        CoroutineScope(Dispatchers.IO).launch {
            socket?.on("userjoinedthechat") { args ->
                try {
                    val data = args[0] as String
                    Log.e("zzzz", "$data")
                } catch (e: Exception) {
                    Log.e("zzzz", e.localizedMessage ?: "")
                }
            }
        }
    }

    fun message() : String{
        var nickname = ""
        CoroutineScope(Dispatchers.IO).launch {
            socket!!.on("message") { args ->
                val data = args[0] as JSONObject
                try {
                    nickname = data.getString("senderNickname")
                    Log.e("zzzz", "$nickname")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        return nickname
    }

    fun disconnect(message : String){
        CoroutineScope(Dispatchers.IO).launch {
            socket?.emit("disconnect", message)
        }
    }

    fun close() {
        CoroutineScope(Dispatchers.IO).launch {
            socket?.close()
            socket = null
        }
    }

    fun isConnected(): Boolean {
        return socket?.isActive ?: false
    }
}