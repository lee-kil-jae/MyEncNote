package com.studiolkj.myencnote.common

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.studiolkj.myencnote.MyApplication
import com.studiolkj.myencnote.R

object Locker {

    @JvmStatic
    fun showRemainLockCount(view : TextView){
        if(MyApplication.prefs.retryCount > 0 && MyApplication.prefs.lockType != 0) {
            view.visibility = View.VISIBLE
            view.setTextColor(ContextCompat.getColor(view.context, R.color.colorTomato))
            val lockCount = when(MyApplication.prefs.lockType) {
                1 -> 30
                2 -> 40
                3 -> 50
                else -> 100
            }
            val remainCount = lockCount - MyApplication.prefs.retryCount
            view.text = String.format(view.context.getString(R.string.remain_infinity_lock_count), remainCount)
        } else {
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    fun hasInfinityLocked(): Boolean {
        if(MyApplication.prefs.lockType == 0){
            return false
        }

        val lockCount = when(MyApplication.prefs.lockType) {
            1 -> 30
            2 -> 40
            3 -> 50
            else -> 100
        }
        return lockCount <= MyApplication.prefs.retryCount
    }

    @JvmStatic
    fun remainLockSec(): Int {
        if (MyApplication.prefs.retryType == 0 || MyApplication.prefs.retryCount < 5) {
            return 0
        }

        var lockSec = when(MyApplication.prefs.retryType) {
            1 -> 5 * (MyApplication.prefs.retryCount - 4)
            2 -> 10 * (MyApplication.prefs.retryCount - 4)
            3 -> 30 * (MyApplication.prefs.retryCount - 4)
            4 -> 60 * (MyApplication.prefs.retryCount - 4)
            else -> 180 * (MyApplication.prefs.retryCount - 4)
        }

        val gap = (System.currentTimeMillis() - MyApplication.prefs.lastRetryTime) / 1000
        return if (gap >= lockSec) { 0 } else { (lockSec - gap).toInt() }
    }

}