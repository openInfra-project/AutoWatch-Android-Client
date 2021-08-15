package com.autowatch.antiseptic

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.autowatch.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.drawer_view

import kotlinx.android.synthetic.main.activity_successroom.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class Successroom : AppCompatActivity() {

    private var dbname: String? = null
    private var dbemail: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successroom)

        //db정보를 가져옴
        val loginDB = loginDB(context = applicationContext)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbname = dbjson.getString("name") ?: null
            dbemail = dbjson.getString("email") ?: null
        } else {

        }
        val name = intent.getStringExtra("roomname")
        val pw = intent.getStringExtra("roompassword")
        val mode= intent.getStringExtra("roommode")



        text_title.setText(name)
        text_password.setText(pw)
        text_mode.setText(mode)
        text_id.setText(dbemail)

        if(mode=="EXAM"){
            btn_enterroom.setText("home")
            btn_enterroom.setOnClickListener {
                startActivity(Intent(this, Home::class.java))
            }

        }else if(mode=="STUDY"){
            btn_enterroom.setOnClickListener {
                enterroom(name)
            }

        }

    }


    fun enterroom(roomname: String) {
        RetrofitClient.signupservice.requestentermyroom(roomname)
            .enqueue(object :
                retrofit2.Callback<DataRoomNumber> {
                override fun onFailure(call: Call<DataRoomNumber>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "전송 실패"+t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                override fun onResponse(
                    call: Call<DataRoomNumber>,
                    response: Response<DataRoomNumber>
                ) {

                    val body = response.body()
                    Log.d("방입장",body?.roomname)
                    //1번이면 얼굴인식 후 방입장
                    //2번이면 바로 방입장
                    //3번이면 방이 없음.

                    if(body!=null) {
                        Toast.makeText(
                            applicationContext,
                            "방 입장합니다",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(applicationContext, Blockapp_study::class.java)
                        intent.putExtra("roomname", roomname)
                        startActivity(intent)

                    }
                }
            })
    }
}