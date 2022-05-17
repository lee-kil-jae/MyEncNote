package com.studiolkj.myencnote

import android.app.Application
import com.studiolkj.myencnote.common.MyEncNotePref
import com.studiolkj.myencnote.common.myDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {

    companion object {
        val TAG = "MyApplication"
        lateinit var prefs: MyEncNotePref
    }

    override fun onCreate() {
        super.onCreate()

        // init Preference
        prefs = MyEncNotePref(applicationContext)

        // init Koin
        startKoin {
            androidContext(applicationContext)
            modules(myDiModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}