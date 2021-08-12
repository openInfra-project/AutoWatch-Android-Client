package com.autowatch.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import kotlinx.android.synthetic.main.activity_settingcamera.*
import kotlinx.android.synthetic.main.activity_test.*
import org.json.JSONObject

class Settingcamera : AppCompatActivity() {
    private var dbname: String? = null
    private var dbemail: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settingcamera)

        val loginDB = loginDB(context = applicationContext)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbname = dbjson.getString("name") ?: null
            dbemail = dbjson.getString("email") ?: null
        } else {

        }


        btn_done.setOnClickListener {
            val sucessintent = Intent(this, Opencv::class.java)
            sucessintent.putExtra("email", dbemail)
            startActivity(sucessintent)
        }

    }
}