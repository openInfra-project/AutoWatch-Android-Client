package com.example.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_choose__login.*

class Choose_Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose__login)
        //관리자 로그인으로 이동
        btn_manager.setOnClickListener {
            btnmanager()
        }

        //사용자 로그인으로 이동
        btn_user.setOnClickListener {
            btnuser()
        }
    }
    private fun btnmanager() {
        startActivity(Intent(this,Manager_Login::class.java))
    }
    private  fun btnuser() {
        startActivity(Intent(this,Login::class.java))
    }
}