package com.autowatch.antiseptic


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_face_recognition.*


class Face_recognition : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_recognition)

        val result = intent.getStringExtra("result")
        Log.d("얼굴인식",result)

        if(result=="no"){
            face.setImageResource(R.drawable.fail)
            result_text1.setText("얼굴 인식 실패 !")
            result_text3.setText("다시 시도해 주세요")
            result_text2.setVisibility(View.INVISIBLE);
            btn_next.setText("Again")
            btn_next.setOnClickListener {
                finish()
            }

        }else{
            btn_next.setOnClickListener {
                startActivity(Intent(this, Blockapp_exam::class.java))
            }
        }
    }

    //back 키 누르면 홈으로
    override fun onBackPressed() {
        startActivity(Intent(this, Home::class.java))
    }

}