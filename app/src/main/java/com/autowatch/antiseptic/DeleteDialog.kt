package com.autowatch.antiseptic

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivities
import kotlinx.android.synthetic.main.activity_delete_dialog.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeleteDialog(context: Context) : Dialog(context) {
    private var dbemail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_dialog)
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        btn_no.setOnClickListener {
            dismiss()
        }
    }

}