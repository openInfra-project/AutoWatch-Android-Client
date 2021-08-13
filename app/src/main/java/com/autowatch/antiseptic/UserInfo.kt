package com.autowatch.antiseptic

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autowatch.antiseptic.data.DataSignUp
import kotlinx.android.synthetic.main.activity_user_info.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class UserInfo : AppCompatActivity() {
    private var dbemail: String? = null
    private var dbpassword: String? = null
    private var dbname: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val loginDB = loginDB(context = applicationContext)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
            dbpassword = dbjson.getString("password") ?: null
        }
        btn_userinfo_backpress.setOnClickListener {
            onBackPressed()
        }
        btn_userinfo_save.setOnClickListener {
            if (edit_userinfo_name.text.toString() != null) {
                login()
            }else {
                Toast.makeText(applicationContext,"입력이 잘못되었습니다",Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun login() {

        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setTitle("업로드중...")
        progressDialog.show()
        RetrofitClient.signupservice.requestModify(
            dbemail!!,
            edit_userinfo_name.text.toString()
        ).enqueue(object : Callback<DataSignUp> {
            override fun onFailure(call: Call<DataSignUp>, t: Throwable) {
                Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DataSignUp>, response: Response<DataSignUp>) {
                //로그인 성공시 홈으로 이동
                val body = response.body()
                if (body!=null) {
                    Toast.makeText(applicationContext,""+response.body(),Toast.LENGTH_LONG).show()
                    val loginDB = loginDB(context = applicationContext)
                    edit_userinfo_name.setText(""+body.name)
                    loginDB.insertDB(dbemail!!,dbpassword!!,body.name,body.image)
                    progressDialog.cancel()
                    Toast.makeText(applicationContext, "홈화면으로 이동합니다", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, Home::class.java))
                }

            }
        })


    }


}