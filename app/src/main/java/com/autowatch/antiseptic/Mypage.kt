package com.autowatch.antiseptic

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.autowatch.antiseptic.data.DataMypage
import com.autowatch.antiseptic.data.DataSignUp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.activity_mypage.btn_roominfo_backpress
import kotlinx.android.synthetic.main.activity_nav_room.*
import kotlinx.android.synthetic.main.activity_user_info.*

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mypage : AppCompatActivity() {
    private var dbemail: String? = null
    private var dbpassword: String? = null
    private var dbname: String? = null
    private var imageurl :String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
        val loginDB = loginDB(context = applicationContext)
//        code_visible.visibility = View.GONE
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
            dbpassword = dbjson.getString("password") ?: null
            dbname = dbjson.getString("name") ?: null
        } else {

        }
        text_user_name.setText("" + dbname + "님 안녕하세요")
        search()
        btn_roominfo_backpress.setOnClickListener {
            onBackPressed()
        }

        Log.d("사용자이미지",imageurl.toString())
    }

    private fun search() {

        dbemail?.let {
            RetrofitClient.signupservice.requestMypage(
                it
            ).enqueue(object : Callback<DataMypage> {
                override fun onFailure(call: Call<DataMypage>, t: Throwable) {
                    Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<DataMypage>, response: Response<DataMypage>) {
                    val body = response.body()
                    if (body!=null) {
                        Toast.makeText(applicationContext,""+response.body(), Toast.LENGTH_LONG).show()
                        user_email.setText(body.email)
                        user_name.setText(body.name)
                        user_date.setText(body.date)
                        imageurl ="https://7d46ea31ac55.ngrok.io/media/"+body.image
                        Log.d("사용자이미지1",body.image)
                        Log.d("사용자이미지2",imageurl)

                        //glide사용할 경우 url이 바뀌지 않으면 갱신되지 않는 문제 발생
                        //디스크캐쉬, 메모리캐쉬 초기화 필요
                        Glide.with(this@Mypage).load(imageurl).apply(
                            RequestOptions()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        ).into(user_image);
                    }

                }

            })
        }


    }
}