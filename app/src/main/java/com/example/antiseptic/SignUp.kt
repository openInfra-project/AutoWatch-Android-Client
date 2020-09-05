package com.example.antiseptic

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    //ViewModel 직렬화를 할 수 없기에
    //imagedata 변수는 사진 선택 후 가져오는 데이터를 넣어줄 임의로 만들어준 변수
    private val viewModel: DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //로그인 하러 가기
        text_goLogin.setOnClickListener {
            GoLogin()
        }

        //회원가입
        btn_enter_signup.setOnClickListener {
            if (edit_email.text.toString() != null && edit_password.text.toString() != null && edit_name.text.toString() != null) {
                //서버로 데이터를 보내줌
                senddata(
                    edit_email.text.toString(),
                    edit_password.text.toString(),
                    edit_name.text.toString()
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


    //RetrofitClient 에 있는 메소드 사용
    private fun senddata(
        email: String,
        password: String,
        name: String
    ) {

        //업로드 중이라는 Dialog 띄어줌
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setTitle("업로드중...")
        progressDialog.show()

        RetrofitClient.signupservice.requestSignUp(
            email,
            password,
            name


        ).enqueue(object : Callback<DataSignUp> {
            override fun onFailure(call: Call<DataSignUp>, t: Throwable) {
                //실패시 작업 -> 간단하게 Dialog 띄어주기
                Toast.makeText(
                    applicationContext,
                    "회원가입 실패",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<DataSignUp>, response: Response<DataSignUp>) {
                //데이터를 받아서 저장해줌.

                val body = response.body()
                if(response.body()?.name!="Fail") {
                    val loginDB = loginDB(context = applicationContext)
                    loginDB.insertDB(body!!.email,body!!.password,body!!.name)
                    Toast.makeText(applicationContext, "홈화면으로 이동합니다", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, Home::class.java))
                    progressDialog.cancel()
                }else {
                    Toast.makeText(applicationContext, "이메일이 중복됩니다", Toast.LENGTH_SHORT).show()
                    progressDialog.cancel()
                }

            }
        })


    }
}