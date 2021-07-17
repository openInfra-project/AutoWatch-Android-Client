package com.autowatch.antiseptic

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_delete_dialog.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteDialog(context:Context) : Dialog(context) {
    private var dbemail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_dialog)
        btn_yes.setOnClickListener {
            deleteUser()
        }
        btn_no.setOnClickListener {
            dismiss()
        }
    }
    fun deleteUser() {
        //dialog 로 확인메세지 한번 표시해주기
        val loginDB = loginDB(context = context)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
        }
        RetrofitClient.signupservice.requestDelete(dbemail!!).enqueue(object : Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(context, "회원탈퇴 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.body() == 200) {
                    Toast.makeText(context, "회원탈퇴 완료되었습니다", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(context, "회원탈퇴 실패", Toast.LENGTH_SHORT).show()
                }

            }
        })

    }
}