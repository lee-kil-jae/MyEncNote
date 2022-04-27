package com.studiolkj.myencnote.common

import android.content.Context
import android.content.SharedPreferences

class MyEncNotePref(context: Context) {

    companion object {
        val FILENAME = "prefs"
        val KEY_HASH_KEY = "hash_key"
        val KEY_RETRY_TYPE = "retry_type"
        val KEY_LOCK_TYPE = "lock_type"
        val KEY_FAIL_COUNT = "fail_count"
        val KEY_HOLDING_TIME = "holding_time"
        val KEY_SECURE_HASK_KEY = "secure_hash_key"
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

    var failCount: Int
        get()  = prefs.getInt(KEY_FAIL_COUNT, 0)
        set(value) = prefs.edit().putInt(KEY_FAIL_COUNT, value).apply()

    var holdingTime: Int
        get()  = prefs.getInt(KEY_HOLDING_TIME, 6)
        set(value) = prefs.edit().putInt(KEY_HOLDING_TIME, value).apply()

    var secureHashKey: String
        get()  = prefs.getString(KEY_SECURE_HASK_KEY, "ze2VUFNR8UMlmTUSrwga50K9F0E=") ?: "ze2VUFNR8UMlmTUSrwga50K9F0E="
        set(value) = prefs.edit().putString(KEY_SECURE_HASK_KEY, value).apply()
}