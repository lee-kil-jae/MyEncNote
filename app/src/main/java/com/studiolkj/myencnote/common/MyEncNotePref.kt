package com.studiolkj.myencnote.common

import android.content.Context
import android.content.SharedPreferences

class MyEncNotePref(context: Context) {

    companion object {
        val FILENAME = "prefs"
        val KEY_ENC_KEY = "enc_key"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, 0)

    var encKey: String
        get()  = prefs.getString(KEY_ENC_KEY, "")?:""
        set(value) = prefs.edit().putString(KEY_ENC_KEY, value).apply()
}