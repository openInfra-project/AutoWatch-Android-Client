package com.autowatch.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.autowatch.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_endroom.*
import kotlinx.android.synthetic.main.activity_settingcamera.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class Endroom : AppCompatActivity() {
    private var dbemail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endroom)

        val nonperson:Int
        var realcount = Accessibility.count
        Log.d("횟수", realcount.toString())
        val person = intent.getStringExtra("nonperson")
        nonperson=person.toInt()
        Log.d("자리이탈횟수", nonperson.toString())
        val roomname = intent.getStringExtra("roomname")



        val loginDB = loginDB(context = applicationContext)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
        }

        send_count(dbemail.toString(),realcount,nonperson,roomname)
        Accessibility.count =0

        btn_home.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
        }

    }

    //back 키 X
    override fun onBackPressed() {
//        startActivity(Intent(this, Home::class.java))
    }


    fun send_count(email:String,count: Int,nonperson:Int,roomname:String) {
        RetrofitClient.signupservice.requestsendcount(email,count,nonperson,roomname)
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
                        "방 퇴장"+response.body(),
                        Toast.LENGTH_LONG
                    ).show()
                    val body = response.body()
                    Log.d("방 퇴장",body?.roomname)

                    if(body!=null) {
                        Toast.makeText(
                            applicationContext,
                            "방을 나갔습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
    }
}