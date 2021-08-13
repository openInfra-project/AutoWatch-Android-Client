package com.autowatch.antiseptic

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
import java.util.*


class ManagerRoomPopUp : AppCompatActivity() {

    val STORAGE_PERMISSOIN_CODE: Int = 1000
    var url = ""
    private var dbname: String? = null
    private var filepath: Uri? = null
    private var dbemail: String? = null
    private var body : MultipartBody.Part?=null
    private var mode : String? = null
    private var randomroomname : String? = null
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

        btn_manageroom_getfile?.setVisibility(View.GONE);
        btn_download?.setVisibility(View.GONE);
        text_makeroom_cell_border?.setVisibility(View.GONE);
        text_makeroom_cell?.setVisibility(View.GONE);
        download_txt.setVisibility(View.GONE);

        randomname()



        //방이름 다시
        btn_remake.setOnClickListener {
            randomname()
        }

        //명단양식 다운로드
        btn_download.setOnClickListener {
            checkVersion("https://docs.google.com/spreadsheets/d/1vd6kFxQnzd-ktx5c29MKMTzQOMctrd15/edit?usp=sharing&ouid=116136621880172225562&rtpof=true&sd=true")
        }



        //뒤로가기 버튼
        btn_roominfo_backpress.setOnClickListener {
            onBackPressed()
        }
        //명단 가져오기
        btn_manageroom_getfile.setOnClickListener {
            onPickDoc()
        }
        //방만들기
        btn_manageroom_makeroom.setOnClickListener {

            when (rg1.checkedRadioButtonId) {
                R.id.rb1 -> mode = "STUDY"
                R.id.rb2 -> mode = "EXAM"

            }
            if(edit_manager_roompassword.text.toString()!="") {
                if (mode == "STUDY") {
                    NameandPassOnly(
                        randomroomname.toString(),
                        edit_manager_roompassword.text.toString(),
                        mode.toString()
                    )
                    Log.d("룸비번", edit_manager_roompassword.getText().toString())
                    val sucessintent = Intent(this, Successroom::class.java)
                    sucessintent.putExtra("roomname", randomroomname)
                    sucessintent.putExtra("roompassword", edit_manager_roompassword.text.toString())
                    sucessintent.putExtra("roommode", mode.toString())
                    startActivity(sucessintent)

                } else if (mode == "EXAM") {
                    if(body!=null) {
                        makeroom(
                            randomroomname.toString(),
                            edit_manager_roompassword.text.toString(),
                            mode.toString()
                        )
                        val sucessintent = Intent(this, Successroom::class.java)
                        sucessintent.putExtra("roomname", randomroomname)
                        sucessintent.putExtra(
                            "roompassword",
                            edit_manager_roompassword.text.toString()
                        )
                        sucessintent.putExtra("roommode", mode.toString())
                        startActivity(sucessintent)
                    }else
                        Toast.makeText(this, "명단을 제출해주세요", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "방 mode를 선택해주세요", Toast.LENGTH_LONG).show()
                }
            }else
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()





        }


        //라디오버튼(study exam mode)
        rg1.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb1 -> {mode = "STUDY"
                    btn_manageroom_getfile?.setVisibility(View.GONE);
                    text_makeroom_cell_border?.setVisibility(View.GONE);
                    btn_download?.setVisibility(View.GONE);
                    text_makeroom_cell?.setVisibility(View.GONE);
                    download_txt.setVisibility(View.GONE);}
                R.id.rb2 -> {mode = "EXAM"
                    btn_manageroom_getfile?.setVisibility(View.VISIBLE);
                    text_makeroom_cell_border?.setVisibility(View.VISIBLE);
                    btn_download?.setVisibility(View.VISIBLE);
                    text_makeroom_cell?.setVisibility(View.VISIBLE);
                    download_txt.setVisibility(View.VISIBLE);}
            }
        }



    }
    private fun checkVersion(url : String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSOIN_CODE)
            }
            else {
                Log.e("다운시작", url)
                startDownloading(url)
                Log.e("다운완료", url)
            }
        }
        else {
            Log.e("다운시작", url)
            startDownloading(url)
            Log.e("다운완료", url)
        }
    }

    private fun startDownloading(url:String) {

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle("명단 양식")
            .setDescription("The file is downloading..")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")
            .allowScanningByMediaScanner()

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            STORAGE_PERMISSOIN_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun randomname() {
        val rnd = Random()

        val buf = StringBuffer()

        for (i in 0..6) {
            if (rnd.nextBoolean()) {
                buf.append((rnd.nextInt(26) as Int + 65).toChar())
            } else {
                buf.append(rnd.nextInt(10))
            }
        }
        Log.d("난수", buf.toString())
        randomroomname = buf.toString()
        text_manager_roomname.setText(randomroomname)
    }

    fun onPickDoc() {
        Log.d("방 모드 확인!!!!!!", mode)
        if(mode=="STUDY")
            Toast.makeText(applicationContext, "EXAM MODE 선택시에만 가능합니다.", Toast.LENGTH_LONG).show()
        else if (mode=="EXAM") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSOIN_CODE)
                }
                else {
                    val intent = Intent()
                    intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    intent.setAction(Intent.ACTION_GET_CONTENT)
                    startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0)
                    Log.d("파일확인1", mode)
                }
            }
            else {
                val intent = Intent()
                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0)
                Log.d("파일확인1", mode)
            }

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
                filepath = data.getData()
                Log.d("파일확인3", filepath.toString())

                filepath?.let { uploadFile(it) }
            }
        }
    }

    fun uploadFile(fileUri: Uri) {
        val file = File(FileUtil.getRealPath(this, fileUri))

        Log.d("파일확인4", file.toString())
        Log.d("파일확인4", file.nameWithoutExtension)
        text_makeroom_cell.setText(file.name)
        val requestFile =
            RequestBody.create(MediaType.parse("*/*"), file)
        body = MultipartBody.Part.createFormData("files", file.name, requestFile)
        Log.d("파일확인5", body.toString())

    }
    //exam 모드
    fun makeroom(name: String, pass: String, mode: String) {
        Log.d("방생성확인!!!!!!", mode)
        val description =
            RequestBody.create(
                okhttp3.MultipartBody.FORM, name
            )
        Log.d("방생성1", description?.toString())
        val description2 =
            RequestBody.create(
                okhttp3.MultipartBody.FORM, pass
            )
        Log.d("방생성2", description2?.toString())
        val description3 =
            RequestBody.create(
                okhttp3.MultipartBody.FORM, dbemail!!
            )
        Log.d("방생성3", dbemail)
        val description4 =
            RequestBody.create(
                okhttp3.MultipartBody.FORM, mode
            )
        Log.d("방생성4", description4?.toString())
        Log.d("방생성5", body?.toString())
        if(body!=null) {
            RetrofitClient.signupservice.requestMakeRoom(
                description,
                description2,
                description3,
                description4,
                body!!
            )
                .enqueue(object :
                    retrofit2.Callback<DataMakeRoom> {
                    override fun onFailure(call: Call<DataMakeRoom>, t: Throwable) {
                        Log.d("방생성실패 1 씨벌왜안대 ", t.toString())
                    }

                    @SuppressLint("ResourceAsColor")
                    override fun onResponse(
                        call: Call<DataMakeRoom>,
                        response: Response<DataMakeRoom>
                    ) {
                        Log.d("방생성성공", body?.toString())


                    }
                })
        }else {
            Toast.makeText(applicationContext, "회원 명단을 업로드 해주세요.", Toast.LENGTH_LONG).show()
        }
    }
    //study 모드
    fun NameandPassOnly(name: String, pass: String, mode: String) {
        Log.d("방생성확인!!!!!!", mode)
        RetrofitClient.signupservice.requestRoomNumberPass(name, pass, dbemail!!, mode)
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