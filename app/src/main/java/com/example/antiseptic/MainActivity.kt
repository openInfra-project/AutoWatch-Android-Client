package com.example.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel:DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (image_rotate.getAnimation() == null) {
            val rotateAnimation : Animation = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.anim_image)
            image_rotate.startAnimation(rotateAnimation)
            image_rotate.setAnimation(rotateAnimation)
        }
        //만약 로그인을 했었다면 바로 Home 으로 이동.
        Handler().postDelayed({
            if(viewModel.data.isEmpty()){
                startActivity(Intent(this, Login::class.java))
            }else {
                startActivity(Intent(this, Home::class.java))
            }

        },1000)
    }
}