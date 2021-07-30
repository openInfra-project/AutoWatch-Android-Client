package com.autowatch.antiseptic

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class Test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        checkAccessibilityPermissions()
        setAccessibilityPermissions()
    }

    fun checkAccessibilityPermissions(): Boolean {
        val accessibilityManager =
            getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val list =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        Log.d("service_test", "size : " + list.size)
        for (i in list.indices) {
            val info = list[i]
            if (info.resolveInfo.serviceInfo.packageName == application.packageName) {
                return true
            }
        }
        return false
    }

    fun setAccessibilityPermissions() {
        val permissionDialog=AlertDialog.Builder(this)
        permissionDialog.setTitle("접근성 권한 설정")
        permissionDialog.setMessage("차단 서비스를 위해 접근성 권한이 필요합니다.")
        permissionDialog.setPositiveButton("허용",
            DialogInterface.OnClickListener { dialog, which ->
                startActivity(Intent(ACTION_ACCESSIBILITY_SETTINGS))
                return@OnClickListener
            }).create().show()
    }
}
