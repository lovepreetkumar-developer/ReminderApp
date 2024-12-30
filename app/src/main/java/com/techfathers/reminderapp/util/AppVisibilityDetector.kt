package com.techfathers.reminderapp.util

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Process
import android.text.TextUtils

class AppVisibilityDetector {

    private var sAppVisibilityCallback: AppVisibilityCallback? = null
    private var sIsForeground = false
    private var sHandler: Handler? = null
    private val MSG_GOTO_FOREGROUND = 1
    private val MSG_GOTO_BACKGROUND = 2

    fun init(app: Application, appVisibilityCallback: AppVisibilityCallback?) {
        checkIsMainProcess(app)
        sAppVisibilityCallback = appVisibilityCallback
        app.registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks())
        sHandler = object : Handler(app.mainLooper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MSG_GOTO_FOREGROUND -> performAppGotoForeground()
                    MSG_GOTO_BACKGROUND -> performAppGotoBackground()
                    else -> {
                    }
                }
            }
        }
    }

    private fun checkIsMainProcess(app: Application) {
        val activityManager = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfoList = activityManager.runningAppProcesses
        var currProcessName: String? = null
        val currPid = Process.myPid()
        //find the process name
        for (processInfo in runningAppProcessInfoList) {
            if (processInfo.pid == currPid) {
                currProcessName = processInfo.processName
            }
        }

        //is current process the main process
        check(
            TextUtils.equals(
                currProcessName,
                app.packageName
            )
        ) { "make sure BgDetector.init(...) called in main process" }
    }

    private fun performAppGotoForeground() {
        if (!sIsForeground && null != sAppVisibilityCallback) {
            sIsForeground = true
            sAppVisibilityCallback!!.onAppGotoForeground()
        }
    }

    private fun performAppGotoBackground() {
        if (sIsForeground && null != sAppVisibilityCallback) {
            sIsForeground = false
            sAppVisibilityCallback!!.onAppGotoBackground()
        }
    }

    interface AppVisibilityCallback {
        fun onAppGotoForeground()
        fun onAppGotoBackground()
    }

    private inner class AppActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
        var activityDisplayCount = 0

        override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {
            sHandler?.removeMessages(MSG_GOTO_FOREGROUND)
            sHandler?.removeMessages(MSG_GOTO_BACKGROUND)
            if (activityDisplayCount == 0) {
                sHandler?.sendEmptyMessage(MSG_GOTO_FOREGROUND)
            }
            activityDisplayCount++
        }

        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityStopped(activity: Activity) {
            sHandler?.removeMessages(MSG_GOTO_FOREGROUND)
            sHandler?.removeMessages(MSG_GOTO_BACKGROUND)
            if (activityDisplayCount > 0) {
                activityDisplayCount--
            }
            if (activityDisplayCount == 0) {
                sHandler?.sendEmptyMessage(MSG_GOTO_BACKGROUND)
            }
        }

        override fun onActivityDestroyed(p0: Activity) {}
    }
}