package com.autowatch.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.autowatch.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_checkmyinfo.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import retrofit2.Call
import retrofit2.Response

class Checkmyinfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkmyinfo)

        // 학번 수험번호, 이름 확인

        enter.setOnClickListener {
            checkinfo(std_number.text.toString(),std_name.text.toString())
        }


    }


    fun checkinfo(number: String, name: String) {
        RetrofitClient.signupservice.requestcheckmyinfo(number, name)
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
                            sucessintent.putExtra("number", number)

                            startActivity(sucessintent)
                        }else if(body.roomname=="no"){
                            //학번 이름 불일치
                            Toast.makeText(
                                applicationContext,
                                "학번과 이름이 일치하지 않습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(body.roomname=="Fail") {
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