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
            ).enqueue(object : Callback<DataSignUp> {
                override fun onFailure(call: Call<DataSignUp>, t: Throwable) {
                    Toast.makeText(applicationContext,"통신 실패",Toast.LENGTH_SHORT).show()
                    text_goSignUp.setText(""+t.message)
                }

                override fun onResponse(call: Call<DataSignUp>, response: Response<DataSignUp>) {
                    //로그인 성공시 홈으로 이동
                    text_goSignUp.setText(""+response.body())
//                    if (response.body()) {
//                        startActivity(Intent(applicationContext, Home::class.java))
//                    } else {
//                        Toast.makeText(applicationContext, "로그인 정보가 일치하지 않습니다", Toast.LENGTH_SHORT)
//                            .show()
//                        text_goSignUp.setText(""+response.body())
//
//                    }
                }
            })


        } else {
            Toast.makeText(this, "이메일과 패스워드를 정확히 기입해주세요", Toast.LENGTH_SHORT).show()
        }

    }
}