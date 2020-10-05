package com.example.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antiseptic.data.DataMyRoomInfo
import com.example.antiseptic.data.DataRoomNamePass
import com.example.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_nav_room.*
import kotlinx.android.synthetic.main.recycle_item.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NavRoom : AppCompatActivity() {
    private var dbemail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_room)
        val loginDB = loginDB(context = applicationContext)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
            dbemail?.let { go(it) }
        } else {

        }
        btn_roominfo_backpress.setOnClickListener {
            onBackPressed()
        }

    }

    fun go(item: String) {
        roominfo_loading.visibility= View.VISIBLE
        RetrofitClient.signupservice.requestmyroom(item)
            .enqueue(object : Callback<List<DataMyRoomInfo>> {


                override fun onFailure(call: Call<List<DataMyRoomInfo>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "통신 실패",
                        Toast.LENGTH_LONG
                    ).show()
                    roominfo_loading.visibility= View.GONE

                }

                override fun onResponse(
                    call: Call<List<DataMyRoomInfo>>,
                    response: Response<List<DataMyRoomInfo>>
                ) {
                    val body = response.body()
                    if (body != null) {
                        roominfo_loading.visibility= View.GONE
                        val adapter = RecyclerAdapter(body, LayoutInflater.from(applicationContext),onClick = {
                            enterroom(it.fields.room_name,it.fields.room_ps)
                        })
                        recycler_view.adapter = adapter
                        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
                    }
                }
            })
    }
    fun enterroom(roomname: String, password: String) {
        RetrofitClient.signupservice.requestenterroom(roomname, password)
            .enqueue(object :
                retrofit2.Callback<DataRoomNumber> {
                override fun onFailure(call: Call<DataRoomNumber>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "전송 실패",
                        Toast.LENGTH_LONG
                    ).show()
                }
                override fun onResponse(
                    call: Call<DataRoomNumber>,
                    response: Response<DataRoomNumber>
                ) {
                    Toast.makeText(
                        applicationContext,
                        "입장",
                        Toast.LENGTH_LONG
                    ).show()
                    val body = response.body()
                    //1번이면 얼굴인식 후 방입장
                    //2번이면 바로 방입장
                    //3번이면 방이 없음.
                    if(body!=null) {
                        if(body.roomname=="1") {
                            Toast.makeText(
                                applicationContext,
                                "얼굴 인식 페이지로 이동합니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(applicationContext, Room::class.java))
                        }else if(body.roomname=="2"){
                            //바로 방입장
                            Toast.makeText(
                                applicationContext,
                                "방 입장합니다",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(body.roomname=="None") {
                            Toast.makeText(
                                applicationContext,
                                "방이 존재하지 않거나 비번이 틀립니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }else {
                            Toast.makeText(
                                applicationContext,
                                "?",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }else {
                        Toast.makeText(
                            applicationContext,
                            "실패",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
    }
}