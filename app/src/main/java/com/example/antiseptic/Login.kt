package com.example.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //회원가입
        text_goSignUp.setOnClickListener {
            GoSignup()
        }
        //로그인
        btn_login_enter.setOnClickListener {
            login()
        }

    }

    //회원가입 하러 가기
    private fun GoSignup() {
        startActivity(Intent(this, SignUp::class.java))
    }

    //로그인
    private fun login() {
        if (edit_login_email.text.toString() != null && edit_login_password.text.toString() != null) {
            RetrofitClient.signupservice.requestLoginIn(
                edit_login_email.text.toString(),
                edit_login_password.text.toString()
            ).enqueue(object : Callback<Int> {
                override fun onFailure(call: Call<Int>, t: Throwable) {

                }

                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    //로그인 성공시 홈으로 이동
                    if (response.body() == 200) {
                        startActivity(Intent(applicationContext, Home::class.java))
                    } else {
                        Toast.makeText(applicationContext, "로그인 정보가 일치하지 않습니다", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
            })

            startActivity(Intent(this, Home::class.java))
        } else {
            Toast.makeText(this, "이메일과 패스워드를 정확히 기입해주세요", Toast.LENGTH_SHORT).show()
        }

    }
}