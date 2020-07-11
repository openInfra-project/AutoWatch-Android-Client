package com.example.antiseptic

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
//            if (edit_email.text.toString() != null && edit_password.text.toString() != null && edit_name.text.toString() != null) {
//                //서버로 데이터를 보내줌
//                senddata(
//                    edit_email.text.toString(),
//                    edit_password.text.toString(),
//                    edit_name.text.toString()
//                //이미지 부분 여기에 적기
//                )


//            } else {
//                Toast.makeText(this, "빈칸없이 입력해주세요", Toast.LENGTH_LONG).show()
//            }
        }


    }


    //로그인 하러 가기
    private fun GoLogin() {
        startActivity(Intent(this, Login::class.java))
    }
    //사진 선택
    private fun PhotoSelect() {
        startActivity(Intent(this, User_SignUp_PopUp::class.java))
    }

    //RetrofitClient 에 있는 메소드 사용
    private fun senddata(email: String, password: String, name: String, image: Image) {
        val retrofit: RetrofitClient = RetrofitClient
        retrofit.signupservice.requestSignUp(
            email,
            password,
            name,
            image//이미지 데이터 여기에 넣기
        ).enqueue(object  : Callback<DataSignUp>{
            override fun onFailure(call: Call<DataSignUp>, t: Throwable) {
                //실패시 작업 -> 간단하게 Dialog 띄어주기
            }

            override fun onResponse(call: Call<DataSignUp>, response: Response<DataSignUp>) {
                //성공시 작업 -> 데이터를 가져와서 ViewModel 에 setdata에 넣어주는게 좋을듯.
                // ->ViewModel 의 데이터를 Main에서(getdata) 가져온 다음 자동으로 Home 으로 넘어감.

            }
        })
    }
}