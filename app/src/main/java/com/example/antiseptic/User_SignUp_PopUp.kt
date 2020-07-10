package com.example.antiseptic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_user__sign_up__pop_up.*

class User_SignUp_PopUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__sign_up__pop_up)
        //뒤로가기 버튼
        btn_popup_close.setOnClickListener {
            Close()
        }
    }
    //뒤로가기 버튼
    fun Close() {
        onBackPressed()
    }
}