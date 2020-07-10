package com.example.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        text_goSignUp.setOnClickListener {
            GoSignup()
        }

    }
    //회원가입 하러 가기
    private fun GoSignup() {
        startActivity(Intent(this,SignUp::class.java))
    }
}