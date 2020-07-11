package com.example.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //로그인 하러 가기
        text_goLogin.setOnClickListener {
            GoLogin()
        }
        //사진 선택
        btn_PhotoSelect.setOnClickListener {
            PhotoSelect()
        }
        //회원가입
        btn_enter_signup.setOnClickListener {

        }


    }

    //로그인 하러 가기
    private fun GoLogin() {
        startActivity(Intent(this, Login::class.java))
    }

    private fun PhotoSelect() {
        startActivity(Intent(this, User_SignUp_PopUp::class.java))
    }

    //RetrofitClient 에 있는 메소드 사용
    private fun senddata() {
        val retrofit: RetrofitClient = RetrofitClient
        retrofit.signupservice.requestSignUp(
            edit_email.text.toString(),
            edit_password.text.toString(),
            edit_name.text.toString(),
            //이미지 데이터 여기에 넣기
        )
    }
}