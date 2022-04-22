package com.studiolkj.myencnote.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.studiolkj.myencnote.viewModel.BaseViewModel

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    lateinit var viewDataBinding: T
    abstract val layoutResourceId: Int
//    var hasFullScreen : Boolean = true
//    var hasSecureViolationOnlyActivityFinish : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
//        setFullScreen()
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
    }

    override fun onResume() {
        super.onResume()
//        setFullScreen()
//        val secureViolation = SecureUtils.checkSecure(this)
//        if(secureViolation != SecureUtils.SECURE_OK){
//            if(hasSecureViolationOnlyActivityFinish) {
//                finish()
//            }else{
//                SecureUtils.finishApp(this, secureViolation)
//            }
//        }
    }

//    fun setFullScreenMode(mode: Boolean){
//        hasFullScreen = mode
//    }
//
//    fun setFullScreen(){
//        if(hasFullScreen) {
//            var flag: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                flag = flag or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                flag = flag or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                flag = flag or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//            }
//            window.decorView.systemUiVisibility = flag
//        }
//    }

    fun initObserve(viewModel : BaseViewModel?){

        viewModel?.back?.observe(this, Observer {
            this.finish()
        })

        viewModel?.toastMessage?.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel?.toastMessage2?.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

}