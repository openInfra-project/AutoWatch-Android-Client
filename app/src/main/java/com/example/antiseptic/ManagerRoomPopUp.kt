package com.example.antiseptic

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton
import android.widget.Toast
import com.example.antiseptic.data.DataImage2
import com.example.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.drawer_view
import kotlinx.android.synthetic.main.nav_header.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerRoomPopUp : AppCompatActivity() {
    private var dbname: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_room_pop_up)
        //db정보를 가져옴
        val loginDB = loginDB(context = applicationContext)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbname = dbjson.getString("name") ?: null
            text_managerroom_name.setText(dbname)
        } else {

        }
        //뒤로가기 버튼
        btn_mangerroom_backpress.setOnClickListener {
            onBackPressed()
        }
        edit_manager_roomname.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                switch_switch.left
                switch_switch.isChecked=false
                switch_switch.isClickable=true
                text_managerroom_check.setText("중복체크 해주시기 바랍니다")
            }

        })
        // 방이름 중복체크
        //edit 수정하거나 다시 입력하면 swtich 원상복구 되야함.
        switch_switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            @SuppressLint("ResourceAsColor")
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    text_managerroom_check.setText("중복체크 완료")
                    text_managerroom_check.setTextColor(R.color.ourColor)
                    switch_switch.isClickable=false
                } else {
                    //체크된 상태로 만들 시 코드
//                    if (edit_manager_roomname.toString() != null) {
//                        RetrofitClient.signupservice.requestRoomNumber(edit_manager_roomname.toString())
//                            .enqueue(object :
//                                Callback<DataRoomNumber> {
//                                override fun onFailure(call: Call<DataRoomNumber>, t: Throwable) {
//                                    Toast.makeText(
//                                        applicationContext,
//                                        "전송 실패",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//
//                                }
//
//                                @SuppressLint("ResourceAsColor")
//                                override fun onResponse(
//                                    call: Call<DataRoomNumber>,
//                                    response: Response<DataRoomNumber>
//                                ) {
//                                    Toast.makeText(
//                                        applicationContext,
//                                        "중복체크 완료",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                    text_managerroom_check.setText("중복체크 완료")
//                                    text_managerroom_check.setTextColor(R.color.ourColor)
//
//                                }
//                            })
                }


            }
        })

    }


}

