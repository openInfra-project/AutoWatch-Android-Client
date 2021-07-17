package com.autowatch.antiseptic

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast


class Accessibility : AccessibilityService() {
    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val denyApp = false;
        if (event?.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if ("com.kakao.talk".equals(event.packageName) || "com.sec.android.app.sbrowser".equals(event.packageName)) {
                Toast.makeText(
                    applicationContext,
                    "앱이 거부되었습니다" + event.packageName,
                    Toast.LENGTH_LONG
                ).show()
                gotoHome();
            }

//            Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
//            Log.e(TAG, "Catch Event TEXT : " + event.getText());
//            Log.e(TAG, "Catch Event ContentDescription : " + event.getContentDescription());
//            Log.e(TAG, "Catch Event getSource : " + event.getSource());
//            Log.e(TAG, "=========================================================================");
        }
    }

    private fun gotoHome() {
        val intent = Intent()
        intent.action = "android.intent.action.MAIN"
        intent.addCategory("android.intent.category.HOME")
        intent.addFlags(
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    or Intent.FLAG_ACTIVITY_FORWARD_RESULT
                    or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                    or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        )
        startActivity(intent)
    }

}

