package com.example.antiseptic

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.view.Gravity
import android.view.Window
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import org.json.JSONObject

class ManagerRoomPopUp : AppCompatActivity() {
    private var dbname : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        //액티비티 애니메이션
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
                // set an slide transition
                exitTransition = Explode()
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_room_pop_up)
        val loginDB = loginDB(context = applicationContext)
        val dbjson : JSONObject = loginDB.getloginDB()
        if(dbjson.length()>0) {
            dbname = dbjson.getString("name")?:null
            text_managerroom_name.setText(dbname)
        }else {

        }


        btn_mangerroom_backpress.setOnClickListener {
            onBackPressed()
        }

    }
}