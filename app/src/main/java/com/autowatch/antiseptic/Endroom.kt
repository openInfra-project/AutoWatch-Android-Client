package com.autowatch.antiseptic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class Endroom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endroom)

        var realcount = Accessibility.count
        Log.d("횟수", realcount.toString())
        Accessibility.count =0
    }
}