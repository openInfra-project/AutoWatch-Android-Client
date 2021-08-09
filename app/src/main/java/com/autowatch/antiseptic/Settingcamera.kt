package com.autowatch.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settingcamera.*
import kotlinx.android.synthetic.main.activity_test.*

class Settingcamera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settingcamera)


        btn_done.setOnClickListener {
            startActivity(Intent(this, Opencv::class.java))
        }

    }
}