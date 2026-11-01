package be.mychat.assistant

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class MyChatAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onServiceConnected() {
        instance = this
        queuedCommand?.let { command ->
            queuedCommand = null
            performCommand(command)
        }
    }

    override fun onDestroy() {
        if (instance === this) instance = null
        super.onDestroy()
    }

    fun performCommand(raw: String) {
        val command = raw.lowercase()
        when {
            command.contains("terug") || command.contains("back") -> performGlobalAction(GLOBAL_ACTION_BACK)
            command.contains("home") || command.contains("startscherm") -> performGlobalAction(GLOBAL_ACTION_HOME)
            command.contains("scroll") || command.contains("naar beneden") -> {
                val root = rootInActiveWindow
                val scrollable = findScrollable(root)
                if (scrollable != null) scrollable.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                else Toast.makeText(this, "Geen scrollbaar onderdeel gevonden.", Toast.LENGTH_SHORT).show()
            }
            command.contains("open") || command.contains("start") || command.contains("ga naar") -> {
                val app = when {
                    command.contains("capcut") -> "com.lemon.lvoverseas"
                    command.contains("tiktok") -> "com.zhiliaoapp.musically"
                    command.contains("youtube") -> "com.google.android.youtube"
                    command.contains("whatsapp") -> "com.whatsapp"
                    command.contains("chrome") -> "com.android.chrome"
                    else -> null
                }
                if (app == null) {
                    Toast.makeText(this, "Ik herken deze app nog niet in deze basisversie.", Toast.LENGTH_LONG).show()
                } else {
                    val launch = packageManager.getLaunchIntentForPackage(app)
                    if (launch != null) {
                        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(launch)
                    } else Toast.makeText(this, "Deze app is niet geïnstalleerd of heeft een andere pakketnaam.", Toast.LENGTH_LONG).show()
                }
            }
            command.startsWith("tik ") || command.startsWith("klik ") -> {
                val target = raw.substringAfter(' ').trim()
                val node = findText(rootInActiveWindow, target)
                if (node != null) node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                else Toast.makeText(this, "Ik kan '$target' niet vinden op het scherm.", Toast.LENGTH_LONG).show()
            }
            else -> Toast.makeText(this, "Deze opdracht is nog niet ondersteund in de basisversie.", Toast.LENGTH_LONG).show()
        }
    }

    private fun findScrollable(node: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        if (node == null) return null
        if (node.isScrollable) return node
        for (i in 0 until node.childCount) {
            val found = findScrollable(node.getChild(i))
            if (found != null) return found
        }
        return null
    }

    private fun findText(node: AccessibilityNodeInfo?, target: String): AccessibilityNodeInfo? {
        if (node == null) return null
        if (node.text?.toString()?.contains(target, ignoreCase = true) == true ||
            node.contentDescription?.toString()?.contains(target, ignoreCase = true) == true) return node
        for (i in 0 until node.childCount) {
            val found = findText(node.getChild(i), target)
            if (found != null) return found
        }
        return null
    }

    companion object {
        @Volatile private var instance: MyChatAccessibilityService? = null
        @Volatile private var queuedCommand: String? = null

        fun executeCommand(command: String) {
            val service = instance
            if (service == null) {
                queuedCommand = command
            } else service.performCommand(command)
        }
    }
}
