package com.autowatch.antiseptic

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.autowatch.antiseptic.data.DataSignUp
import com.autowatch.antiseptic.data.DataViewModel
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private val viewModel: DataViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //회원가입
        btn_goSignUp.setOnClickListener {
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
            val progressDialog: ProgressDialog = ProgressDialog(this)
            progressDialog.setTitle("로그인중...")
            progressDialog.show()
            RetrofitClient.signupservice.requestLoginIn(
                edit_login_email.text.toString(),
                edit_login_password.text.toString()
            ).enqueue(object : Callback<DataSignUp> {
                override fun onFailure(call: Call<DataSignUp>, t: Throwable) {
                    Toast.makeText(applicationContext,"통신 실패",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<DataSignUp>, response: Response<DataSignUp>) {
                    //로그인 성공시 홈으로 이동
                    val body = response.body()
                    if(response.body()?.name!="Fail") {
                        val loginDB = loginDB(context = applicationContext)
                        loginDB.insertDB(body!!.email,body!!.password,body!!.name)
                        Toast.makeText(applicationContext, "홈화면으로 이동합니다", Toast.LENGTH_LONG).show()
                        startActivity(Intent(applicationContext, Home::class.java))
                    }else {
                        Toast.makeText(applicationContext, "이메일 및 패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                        progressDialog.cancel()
                    }

                }
            })


        } else {
            Toast.makeText(this, "이메일과 패스워드를 정확히 기입해주세요", Toast.LENGTH_SHORT).show()
        }

    }

}