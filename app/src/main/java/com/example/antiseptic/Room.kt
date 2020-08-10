package com.example.antiseptic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import java.io.*
import java.lang.Exception
import java.net.Socket

class Room : AppCompatActivity() {
    var html = ""
    var Handler: Handler? = null
    var socket: Socket? = null
    var BufferedReader: BufferedReader? = null
    var BufferedWriter: BufferedWriter? = null
    val ip = "127.0.0.0.3000"
    val port = 9000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        Handler = Handler()
        try {
            setSocket(ip, port)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        checkUpdate.start()
    }

    override fun onStop() {
        super.onStop()
        try {
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    val checkUpdate: Thread = Thread() {
        fun run() {
            try {
                var line: String? = null
                while (true) {
                    line=BufferedReader!!.readLine()
                    html=line
                    Handler?.post(showUpdate)

                }
            }catch (e:Exception){

            }
        }
    }
    val showUpdate:Runnable=Runnable() {
        fun run() {

        }
    }

    fun setSocket(ip: String, port: Int) {
        try {
            socket = Socket(ip, port)
            BufferedReader = BufferedReader(InputStreamReader(socket?.getInputStream()))
            BufferedWriter = BufferedWriter(OutputStreamWriter(socket?.getOutputStream()))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
