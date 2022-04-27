package com.studiolkj.myencnote.model

import com.studiolkj.myencnote.MyApplication

object InstancePassword {
    var password: String = ""
    var checkTime = 0L

    @JvmName("setPassword1")
    fun setPassword(passwd: String) {
        password = passwd
        checkTime = System.currentTimeMillis()
    }

    @JvmName("getPassword1")
    fun getPassword(): String {
        val gap = System.currentTimeMillis() - checkTime
        if (!password.isNullOrEmpty() && gap > getHoldingTime()) {
            password = ""
        }
        return password
    }

    private fun getHoldingTime(): Int {
        return when (MyApplication.prefs.holdingTime) {
            0 -> 0
            1 -> 10000
            2 -> 20000
            3 -> 30000
            4 -> 40000
            5 -> 50000
            6 -> 60000
            7 -> 120000
            else -> 180000
        }

    }
}