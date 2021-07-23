package com.autowatch.antiseptic

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autowatch.antiseptic.data.DataMakeRoom
import com.autowatch.antiseptic.data.DataRoomNamePass
import com.autowatch.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File


class ManagerRoomPopUp : AppCompatActivity() {
    private var dbname: String? = null
    private var filepath: Uri? = null
    private var dbemail: String? = null
    private var body : MultipartBody.Part?=null
    private var mode : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_room_pop_up)
        //db정보를 가져옴
        val loginDB = loginDB(context = applicationContext)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbname = dbjson.getString("name") ?: null
            dbemail = dbjson.getString("email") ?: null
            text_managerroom_name.setText(dbname)
        } else {

        }


        //뒤로가기 버튼
        btn_mangerroom_backpress.setOnClickListener {
            onBackPressed()
        }
        //명단 가져오기
        btn_manageroom_getfile.setOnClickListener {
            onPickDoc()
        }
        //방만들기
        btn_manageroom_makeroom.setOnClickListener {

            when (rg1.checkedRadioButtonId) {
                R.id.rb1 -> mode = "1"
                R.id.rb2 -> mode = "2"
            }
            if(edit_manager_roomname.text.toString()!=null && edit_manager_roompassword.text.toString()!=null && body==null) {
                NameandPassOnly(edit_manager_roomname.text.toString(),edit_manager_roompassword.text.toString(),mode.toString())
            }
            else if(edit_manager_roomname.text.toString()!=null && edit_manager_roompassword.text.toString()!=null&&body!=null){
                makeroom(edit_manager_roomname.text.toString(),edit_manager_roompassword.text.toString(),mode.toString())
            }else {
                Toast.makeText(this,"방이름 및 비번을 입력해주세요",Toast.LENGTH_LONG).show()
            }
        }

        //라디오버튼(study exam mode)
        rg1.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb1 -> mode = "1"
                R.id.rb2 -> mode = "2"
            }
        }
        //edit listener
        edit_manager_roomname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                switch_switch.left
                switch_switch.isChecked = false
                switch_switch.isClickable = true
                text_managerroom_check.setText("중복체크 해주시기 바랍니다")
            }

        })
        //edit 수정하거나 다시 입력하면 swtich 원상복구 되야함.
        switch_switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            @SuppressLint("ResourceAsColor")
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    switch_switch.isClickable = false
                    if (edit_manager_roomname.text.toString() != null) {
                        RetrofitClient.signupservice.requestRoomNumber(edit_manager_roomname.text.toString())
                            .enqueue(object :
                                retrofit2.Callback<DataRoomNumber> {
                                override fun onFailure(call: Call<DataRoomNumber>, t: Throwable) {
                                    Toast.makeText(
                                        applicationContext,
                                        "전송 실패",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                @SuppressLint("ResourceAsColor")
                                override fun onResponse(
                                    call: Call<DataRoomNumber>,
                                    response: Response<DataRoomNumber>
                                ) {
                                    Toast.makeText(
                                        applicationContext,
                                        "중복체크 완료",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    text_managerroom_check.setText("중복체크 완료")
                                    text_managerroom_check.setTextColor(R.color.ourColor)

                                }
                            })
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "빈칸없이 입력해주시기 바랍니다",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    //체크된 상태로 만들 시 코드


                }
            }
        })


    }

    fun onPickDoc() {
        Log.d("방 모드 확인!!!!!!",mode)
        if(mode=="1")
            Toast.makeText(applicationContext, "EXAM MODE 선택시에만 가능합니다.", Toast.LENGTH_LONG).show()
        else if (mode=="2") {
            val intent = Intent()
            intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0)
            Log.d("파일확인1",mode)
        }
        else
            Toast.makeText(applicationContext, "EXAM MODE 선택시에만 가능합니다.", Toast.LENGTH_LONG).show()
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Log.d("파일확인2", data.toString())
                filepath = data.data
                Log.d("파일확인3", filepath.toString())
                text_makeroom_cell.setText("회원명단목록")
                filepath?.let { uploadFile(it) }
            }
        }
    }

    fun uploadFile(fileUri: Uri) {
        val file = File(FileUtil.getPath(fileUri,this))
        Log.d("파일확인4", file.toString())
        val requestFile =
            RequestBody.create(MediaType.parse(contentResolver.getType(fileUri)), file)
        body = MultipartBody.Part.createFormData("files", file.name, requestFile)
        Log.d("파일확인5", body.toString())

    }
    //exam 모드
    fun makeroom(name:String,pass:String,mode:String) {
        val description =
            RequestBody.create(
                okhttp3.MultipartBody.FORM, name)
        val description2 =
            RequestBody.create(
                okhttp3.MultipartBody.FORM, pass)
        val description3 =
            RequestBody.create(
                okhttp3.MultipartBody.FORM, dbemail)
        val description4 =
            RequestBody.create(
                okhttp3.MultipartBody.FORM, mode)
        if(body!=null) {
            RetrofitClient.signupservice.requestMakeRoom(body!!,description,description2,description3,description4)
                .enqueue(object :
                    retrofit2.Callback<DataMakeRoom> {
                    override fun onFailure(call: Call<DataMakeRoom>, t: Throwable) {

                    }
                    override fun onResponse(
                        call: Call<DataMakeRoom>,
                        response: Response<DataMakeRoom>
                    ) {

                    }
                })
        }else {
            Toast.makeText(applicationContext, "회원 명단을 업로드 해주세요.", Toast.LENGTH_LONG).show()
        }
    }
    //study 모드
    fun NameandPassOnly(name:String,pass:String,mode:String) {
        Log.d("방생성확인!!!!!!",mode)
        RetrofitClient.signupservice.requestRoomNumberPass(name,pass,dbemail!!,mode)
            .enqueue(object :
                retrofit2.Callback<DataRoomNamePass> {
                override fun onFailure(call: Call<DataRoomNamePass>, t: Throwable) {

                }

                @SuppressLint("ResourceAsColor")
                override fun onResponse(
                    call: Call<DataRoomNamePass>,
                    response: Response<DataRoomNamePass>
                ) {


                }
            })

    }

}


