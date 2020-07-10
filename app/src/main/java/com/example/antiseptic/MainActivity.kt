package com.example.antiseptic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (image_rotate.getAnimation() == null) {
            val rotateAnimation : Animation = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.anim_image)
            image_rotate.startAnimation(rotateAnimation)
            image_rotate.setAnimation(rotateAnimation)
        }
        Handler().postDelayed({
            startActivity(Intent(this, Choose_Login::class.java))
        },1000)
    }
}