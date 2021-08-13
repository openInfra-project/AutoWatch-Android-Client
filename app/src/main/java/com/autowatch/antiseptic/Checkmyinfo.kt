package com.autowatch.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.autowatch.antiseptic.data.DataRoomNamePass
import com.autowatch.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_checkmyinfo.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import retrofit2.Call
import retrofit2.Response

class Checkmyinfo : AppCompatActivity() {
    private var roomname: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkmyinfo)

        // 학번 수험번호, 이름 확인

        roomname= intent.getStringExtra("roomname")
        Log.d("신원확인!!!!!!!!!!!!",roomname)
        btn_next.setOnClickListener {
            checkinfo(std_number.text.toString(),std_name.text.toString())
        }


    }
    //back 키 누르면 홈으로
    override fun onBackPressed() {
        startActivity(Intent(this, Home::class.java))
    }


    fun checkinfo(number: String, name: String) {
        RetrofitClient.signupservice.requestcheckmyinfo(roomname.toString(),number, name)
            .enqueue(object :
                retrofit2.Callback<DataRoomNamePass> {
                override fun onFailure(call: Call<DataRoomNamePass>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "전송 실패"+t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                override fun onResponse(
                    call: Call<DataRoomNamePass>,
                    response: Response<DataRoomNamePass>
                ) {
                    Toast.makeText(
                        applicationContext,
                        "입장"+response.body(),
                        Toast.LENGTH_LONG
                    ).show()
                    val body = response.body()
                    Log.d("신원확인",body?.roomname)

                    if (body != null) {
                        //학번 이름 일치
                        if(body.roomname=="yes") {
                            Toast.makeText(
                                applicationContext,
                                "얼굴 인식 페이지로 이동합니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            val sucessintent = Intent(applicationContext, Room::class.java)
                            sucessintent.putExtra("index", body.password)
                            sucessintent.putExtra("roomname", roomname)
                            startActivity(sucessintent)
                        }else if(body.roomname=="no"){
                            //학번 이름 불일치
                            Toast.makeText(
                                applicationContext,
                                "학번과 이름이 일치하지 않습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(body.roomname=="fail") {
                            //명단에 없음
                            Toast.makeText(
                                applicationContext,
                                "명단에 존재하지 않습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                    }
                    }else {
                        Toast.makeText(
                            applicationContext,
                            "실패"+response.body(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
    }
}