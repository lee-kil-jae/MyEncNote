package com.studiolkj.myencnote.common

import android.content.Context
import android.content.SharedPreferences

class MyEncNotePref(context: Context) {

    companion object {
        val FILENAME = "prefs"
        val KEY_HASH_KEY = "hash_key"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, 0)

    var hashKey: String
        get()  = prefs.getString(KEY_HASH_KEY, "")?:""
        set(value) = prefs.edit().putString(KEY_HASH_KEY, value).apply()
}