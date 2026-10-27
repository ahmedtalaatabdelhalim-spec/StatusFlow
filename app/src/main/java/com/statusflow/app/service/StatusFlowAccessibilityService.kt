package com.statusflow.app.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class StatusFlowAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName == "com.whatsapp") {
            val rootNode = rootInActiveWindow ?: return
            findAndClickSendButton(rootNode)
        }
    }

    private fun findAndClickSendButton(node: AccessibilityNodeInfo) {
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val text = child.text?.toString()?.lowercase() ?: ""
            val desc = child.contentDescription?.toString()?.lowercase() ?: ""

            if (text.contains("send") || text.contains("إرسال") || desc.contains("send")) {
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                Log.d("StatusFlowAutomation", "Successfully clicked WhatsApp send/publish button via AccessibilityService.")
                break
            }
            findAndClickSendButton(child)
        }
    }

    override fun onInterrupt() {
        Log.d("StatusFlowAutomation", "Accessibility service interrupted.")
    }
}
