package com.example.antiseptic

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.nav_header.*

class Nav : AppCompatActivity() {
    private val viewModel : DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header)
        //nav 로그인시 화면 , 로그아웃 시 화면
        if(viewModel.data.isEmpty()) {
            text_nav_login.setText("로그인하러가기")
            text_nav_login.setOnClickListener {
                startActivity(Intent(this,Login::class.java))
            }
            text_nav_name.setText("")
            linear_nav_login.visibility=View.GONE
            btn_nav_visible.visibility=View.VISIBLE
        }else {
            text_nav_login.setText("환영합니다")
            text_nav_name.setText(""+viewModel.data[0].name)
            //로그아웃 시 다시 Home 으로 이동 후 바뀐 nav 보여줌
            btn_nav_logout.setOnClickListener {
                viewModel.data.clear()
                startActivity(Intent(this,Home::class.java))
            }
            linear_nav_login.visibility=View.VISIBLE
            btn_nav_visible.visibility=View.GONE
        }
        viewModel.LiveData.observe(this, Observer {
            viewModel.data
        })
    }
}