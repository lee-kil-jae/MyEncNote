package com.studiolkj.myencnote.common

import android.content.Context
import android.content.SharedPreferences

class MyEncNotePref(context: Context) {

    companion object {
        val FILENAME = "prefs"
        val KEY_HASH_KEY = "hash_key"
        val KEY_RETRY_TYPE = "retry_type"
        val KEY_LOCK_TYPE = "lock_type"
        val KEY_HOLDING_TIME = "holding_time"
        val KEY_SECURE_HASK_KEY = "secure_hash_key"

        val KEY_RETRY_COUNT = "retry_count"
        val KEY_LAST_RETRY_TIME = "last_retry_time"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, 0)

    var hashKey: String
        get()  = prefs.getString(KEY_HASH_KEY, "")?:""
        set(value) = prefs.edit().putString(KEY_HASH_KEY, value).apply()

    var retryType: Int
        get()  = prefs.getInt(KEY_RETRY_TYPE, 0)
        set(value) = prefs.edit().putInt(KEY_RETRY_TYPE, value).apply()

    var lockType: Int
        get()  = prefs.getInt(KEY_LOCK_TYPE, 0)
        set(value) = prefs.edit().putInt(KEY_LOCK_TYPE, value).apply()

    var retryCount: Int
        get()  = prefs.getInt(KEY_RETRY_COUNT, 0)
        set(value) = prefs.edit().putInt(KEY_RETRY_COUNT, value).apply()

    var lastRetryTime: Long
        get()  = prefs.getLong(KEY_LAST_RETRY_TIME, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_RETRY_TIME, value).apply()

    var holdingTime: Int
        get()  = prefs.getInt(KEY_HOLDING_TIME, 6)
        set(value) = prefs.edit().putInt(KEY_HOLDING_TIME, value).apply()

    var secureHashKey: String
        get()  = prefs.getString(KEY_SECURE_HASK_KEY, "\$2a\$11\$loaOZMJYnn6fdFBPomtSVOvfDzqU4AjKWm8p978nPx3RPY/cKmPwa") ?: "\$2a\$11\$loaOZMJYnn6fdFBPomtSVOvfDzqU4AjKWm8p978nPx3RPY/cKmPwa"
        set(value) = prefs.edit().putString(KEY_SECURE_HASK_KEY, value).apply()
}