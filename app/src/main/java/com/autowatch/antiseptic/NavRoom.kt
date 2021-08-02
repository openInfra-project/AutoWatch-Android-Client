package com.autowatch.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowatch.antiseptic.data.DataMyRoomInfo
import com.autowatch.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_nav_room.*
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
                    Log.d("내방 보여주기1", response.toString())
                    Log.d("내방 보여주기2", body.toString())
                    if (body != null) {
                        roominfo_loading.visibility= View.GONE
                        val adapter = RecyclerAdapter(body, LayoutInflater.from(applicationContext),onClick = {
                            enterroom(it.fields.room_name,it.fields.room_password)
                            Log.d("방 입장",it.fields.room_name)
                            Log.d("방 입장",it.fields.room_password)

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
                    //1번이면 바로 방입장
                    //2번이면 바로 방입장
                    //3번이면 방이 없음.
                    if(body!=null) {
//                        btn_home_inner.setText(""+body.roomname)
                        if(body.roomname=="STUDY") {  //room abc abc func(aaa)
                            Toast.makeText(
                                applicationContext,
                                "방 입장합니다",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(body.roomname=="EXAM"){   //room v v func(yulime0605)
                            //바로 방입장
                            Toast.makeText(
                                applicationContext,
                                "방 입장합니다",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(body.roomname=="Fail") {
                            Toast.makeText(
                                applicationContext,
                                "방 비밀번호가 틀립니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(body.roomname=="None") {
                            Toast.makeText(
                                applicationContext,
                                "해당 방이 없습니다.",
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
                            "실패"+response.body(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
    }
}