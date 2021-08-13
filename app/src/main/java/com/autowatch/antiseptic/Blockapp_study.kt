package com.autowatch.antiseptic

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.autowatch.antiseptic.data.DataRoomNumber
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_test.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*


class Blockapp_study : AppCompatActivity() {
    private var dbemail: String? = null
    var result:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        val loginDB = loginDB(context = applicationContext)
//        code_visible.visibility = View.GONE
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
        }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        result= checkAccessibilityPermissions()
        Log.d("접근성확인", result.toString())

        val roomname = intent.getStringExtra("roomname")

        if(result==false) {
            setAccessibilityPermissions()

        }else {
            android.setImageResource(R.drawable.no_phone)
            block.setText("---------SNS 앱이 차단 되었습니다---------")
        }
        btn_test.setOnClickListener {
            if(result==true) {

                check(dbemail.toString())
                val intent = Intent(applicationContext, Settingcamera::class.java)
                intent.putExtra("roomname", roomname)
                startActivity(intent)

            }
            else {
                Toast.makeText(
                    applicationContext,
                    "접근성 허용해주세요",
                    Toast.LENGTH_LONG
                ).show()
                setAccessibilityPermissions()
            }


        }


    }
    //back 키 누르면 홈으로
    override fun onBackPressed() {
        startActivity(Intent(this, Home::class.java))
    }

    override fun onResume() {
        super.onResume()
        result = checkAccessibilityPermissions()
        Log.d("접근성확인!!!!", result.toString())
        if(result==true) {
            android.setImageResource(R.drawable.no_phone)
            block.setText("---------SNS 앱이 차단 되었습니다---------")
        }

    }

    fun checkAccessibilityPermissions(): Boolean {
        val accessibilityManager =
            getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val list =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        Log.d("service_test", "size : " + list.size)
        for (i in list.indices) {
            val info = list[i]
            Log.d("service_test", "size : " + info.resolveInfo.serviceInfo.packageName)
            if (info.resolveInfo.serviceInfo.packageName == application.packageName) {

                return true
            }
        }
        return false
    }

    fun setAccessibilityPermissions() {

        val permissionDialog=AlertDialog.Builder(this)
        permissionDialog.setTitle("접근성 권한 설정")
        permissionDialog.setMessage("차단 서비스를 위해 접근성 권한이 필요합니다.")
        permissionDialog.setPositiveButton("허용",
            DialogInterface.OnClickListener { dialog, which ->

                startActivity(Intent(ACTION_ACCESSIBILITY_SETTINGS))   //접근성
                return@OnClickListener

            }).create().show()

    }

    fun check(email: String) {
        RetrofitClient.signupservice.requestcheckin(email)
            .enqueue(object :
                retrofit2.Callback<DataRoomNumber> {
                override fun onFailure(call: Call<DataRoomNumber>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "전송 실패" + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<DataRoomNumber>,
                    response: Response<DataRoomNumber>
                ) {

                    val body = response.body()
                    Log.d("앱 인증 완료", body?.roomname)

                    if (body != null) {
                        Toast.makeText(
                            applicationContext,
                            "방 입장",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "방 입장 실패",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
    }
}
