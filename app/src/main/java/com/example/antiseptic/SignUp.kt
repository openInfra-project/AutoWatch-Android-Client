package com.example.antiseptic

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.antiseptic.RetrofitClient.retrofit
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.net.URLEncoder.*

class SignUp : AppCompatActivity() {
    //ViewModel 직렬화를 할 수 없기에
    //imagedata 변수는 사진 선택 후 가져오는 데이터를 넣어줄 임의로 만들어준 변수
    private lateinit var imageData: ArrayList<DataImage>
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
            if (edit_email.text.toString() != null && edit_password.text.toString() != null && edit_name.text.toString() != null /*&& imageData != null*/) {
                //서버로 데이터를 보내줌
                senddata(
                    edit_email.text.toString(),
                    edit_password.text.toString(),
                    edit_name.text.toString()
                    //imageData
                    //이미지 부분 여기에 적기
                    //이미지를 imagepicker 객체로 보내야 하는지 혹은
                    //uri 로 변환해서 보내야 하는지
                )

            } else {
                Toast.makeText(this, "빈칸없이 입력해주세요", Toast.LENGTH_LONG).show()
            }


        }


    }



    //로그인 하러 가기
    private fun GoLogin() {
        startActivity(Intent(this, Login::class.java))

    }

    //사진 선택
    private fun PhotoSelect() {
        val intent = Intent(this, User_SignUp_PopUp::class.java)
        startActivityForResult(intent, 200)
    }

    //사진 선택 후 돌아오는 데이터 받기

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                imageData = data?.getSerializableExtra("Image") as ArrayList<DataImage>
                text_goLogin.setText("" + imageData)
            }
        } else {

        }
    }


    //RetrofitClient 에 있는 메소드 사용
    private fun senddata(
        email: String,
        password: String,
        name: String
        // image: ArrayList<DataImage>
    ) {

        //업로드 중이라는 Dialog 띄어줌

        RetrofitClient.signupservice.requestSignUp(
            email,
            password,
            name
            //image//이미지 데이터 여기에 넣기
        ).enqueue(object : Callback<DataSignUp> {
            override fun onFailure(call: Call<DataSignUp>, t: Throwable) {
                //실패시 작업 -> 간단하게 Dialog 띄어주기
                Toast.makeText(
                    applicationContext,
                    "Fail" + email + password + name,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<DataSignUp>, response: Response<DataSignUp>) {
                //성공시 작업 -> 데이터를 가져와서 ViewModel 에 setdata에 넣어주는게 좋을듯.
                // ->ViewModel 의 데이터를 Main에서(getdata) 가져온 다음 자동으로 Home 으로 넘어감.
                Toast.makeText(applicationContext, "Success" + email, Toast.LENGTH_LONG).show()
            }
        })
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setTitle("업로드중...")
        progressDialog.show()
    }
}