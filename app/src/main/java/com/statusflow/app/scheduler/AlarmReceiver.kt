package com.statusflow.app.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("StatusFlow", "Alarm received for scheduled status execution.")
        // Here StatusFlow attempts to launch WhatsApp sharing intent or trigger AccessibilityService automation.
        // If device is locked, falls back to posting an actionable notification.
    }
}
