package com.studiolkj.myencnote.common

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Base64
import android.widget.Toast
import com.studiolkj.myencnote.MyApplication
import com.studiolkj.myencnote.R
import java.security.MessageDigest

object SecureUtils {

    @JvmStatic
    fun checkSecure(activity: Activity) {
        if(isCrack(activity)){
            Toast.makeText(activity, R.string.secure_exception_crack, Toast.LENGTH_SHORT).show()
            finishApp(activity)
            return
        }

        if(isRooting()){
            Toast.makeText(activity, R.string.secure_exception_rooting, Toast.LENGTH_SHORT).show()
            finishApp(activity)
            return
        }

        if(isDebugEnable(activity) && isUsbConnected(activity)) {
            Toast.makeText(activity, R.string.secure_exception_debugging, Toast.LENGTH_SHORT).show()
            finishApp(activity)
            return
        }
    }

    @JvmStatic
    fun finishApp(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.finishAndRemoveTask()
        } else {
            activity.finish()
        }
        System.exit(0)
    }

    @JvmStatic
    fun getHashKey(activity: Activity): String? {
        try {
            val packageInfo = activity.packageManager.getPackageInfo(
                activity.packageName,
                PackageManager.GET_SIGNATURES
            )
            val signature = packageInfo.signatures[0]
            val messageDigest = MessageDigest.getInstance("SHA")
            messageDigest.update(signature.toByteArray())
//            Log.d("SIGNATURE", "   - getHashKey: " + String(Base64.encode(messageDigest.digest(), Base64.DEFAULT)));
            return String(Base64.encode(messageDigest.digest(), Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun isCrack(activity: Activity): Boolean {
        return MyApplication.prefs.secureHashKey.equals(getHashKey(activity))
    }

    private fun isRooting(): Boolean {
        var flag = false
        try {
            Runtime.getRuntime().exec("su")
            flag = true
        } catch (ex: Exception) {
        }
        return flag
    }

    private fun isDebugEnable(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) != 0
        } else Settings.Secure.getInt(context.contentResolver, Settings.Secure.ADB_ENABLED, 0) != 0
    }

    private fun isUsbConnected(context: Context): Boolean {
        val intent =
            context.registerReceiver(null, IntentFilter("android.hardware.usb.action.USB_STATE"))
        return intent != null && intent.extras != null && intent.getBooleanExtra(
            "connected",
            false
        ) == true
    }
}