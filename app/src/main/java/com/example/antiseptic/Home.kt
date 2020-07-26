package com.example.antiseptic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.*

class Home : AppCompatActivity() {
    private var its: Boolean = true
    private val viewModel : DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        code_visible.visibility = View.GONE
        //뒤로가기 버튼
        btn_close_Home.setOnClickListener {
            onBackPressed()
        }
        //방입장 버튼 클릭시
        btn_home_join.setOnClickListener {
            homeAnimation(it = its)

        }
        //도움말 버튼 애니메이션
        val animation  = AnimationUtils.loadAnimation(this,R.anim.home_highlight)
        image_home_highlight.startAnimation(animation)
        val animationbackground = AnimationUtils.loadAnimation(this,R.anim.home_highlight_background)
        Linear_background.startAnimation(animationbackground)
        //도움말 Dialog 여기서 구현.
        //menu 애니메이션
        btn_home_menu.setOnClickListener {
            drawer_view.openDrawer(nav_view)
        }
        btn_nav_close.setOnClickListener {
            drawer_view.closeDrawers()
        }
        drawer_view.setOnTouchListener { v, event -> true}




    }

    //버튼클릭시 애니메이션 효과
    fun homeAnimation(it: Boolean) {
        if (it) {
            val animation: TranslateAnimation = TranslateAnimation(
                0F,
                0F,
                0F,
                60F
            )
            animation.duration = 500
            animation.fillAfter = true
            btn_home_create.startAnimation(animation)
            code_visible.startAnimation(animation)
            code_visible.visibility = View.VISIBLE
            its = false

        } else {
            val animation: TranslateAnimation = TranslateAnimation(
                0F,
                0F,
                0F,
                -40F
            )
            animation.duration = 500
            animation.fillAfter = true
            btn_home_create.startAnimation(animation)
            code_visible.startAnimation(animation)
            code_visible.visibility = View.GONE
            its = true
        }
    }


}


