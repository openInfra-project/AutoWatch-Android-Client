package com.autowatch.antiseptic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.autowatch.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.drawer_view

import kotlinx.android.synthetic.main.activity_successroom.*
import kotlinx.android.synthetic.main.nav_roomheader.*
import retrofit2.Call
import retrofit2.Response

class Successroom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successroom)
        val name = intent.getStringExtra("roomname")
        val pw = intent.getStringExtra("roompassword")
        val mode= intent.getStringExtra("roommode")



        text_title.setText(name)
        text_password.setText(pw)
        text_mode.setText(mode)

        btn_enterroom.setOnClickListener {
            enterroom(name)
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
                    Toast.makeText(
                        applicationContext,
                        "입장"+response.body(),
                        Toast.LENGTH_LONG
                    ).show()
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

                    }
                }
            })
    }
}