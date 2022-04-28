package com.studiolkj.myencnote.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.studiolkj.myencnote.MyApplication
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.common.EventMemoUpdate
import com.studiolkj.myencnote.common.SecureUtils
import com.studiolkj.myencnote.databinding.ActivitySettingBinding
import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.model.main.ListAdapterMemo
import com.studiolkj.myencnote.view.dialog.DialogCheckPassword
import com.studiolkj.myencnote.view.dialog.DialogInputPassword
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.mindrot.jbcrypt.BCrypt

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    companion object {
        val TAG = "SettingActivity"
    }

    override val layoutResourceId: Int get() = R.layout.activity_setting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmSetting = getViewModel()
        viewDataBinding.lifecycleOwner = this

        tvVersion.text = getString(R.string.app_version) + " " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName
        initObserve(viewDataBinding.vmSetting)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}