package com.studiolkj.myencnote.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Observer
import com.studiolkj.myencnote.MyApplication
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.common.EventMemoUpdate
import com.studiolkj.myencnote.common.Locker
import com.studiolkj.myencnote.common.SecureUtils
import com.studiolkj.myencnote.common.Utils
import com.studiolkj.myencnote.databinding.ActivityMainBinding
import com.studiolkj.myencnote.model.InstancePassword
import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.model.main.ListAdapterMemo
import com.studiolkj.myencnote.view.dialog.DialogCheckPassword
import com.studiolkj.myencnote.view.dialog.DialogInputPassword
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.mindrot.jbcrypt.BCrypt

class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object {
        val TAG = "MainActivity"
    }

    override val layoutResourceId: Int get() = R.layout.activity_main

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmMain = getViewModel()
        viewDataBinding.lifecycleOwner = this

        initObserve(viewDataBinding.vmMain)

        if (MyApplication.prefs.hashKey.isNullOrEmpty()) {
            showInputPassword()
        }

        viewDataBinding.vmMain?.addMemo?.observe(this, Observer {
            startActivity(Intent(this@MainActivity, AddMemoActivity::class.java))
        })

        viewDataBinding.vmMain?.showMemo?.observe(this, Observer {
            showMemo(it)
        })

        viewDataBinding.vmMain?.showSetting?.observe(this, Observer {
            showSetting()
        })

        EventBus.getDefault().register(this)

        load()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        InstancePassword.setPassword("")
        EventBus.getDefault().unregister(this)
    }

    private fun showSetting(){
        if(!InstancePassword.getPassword().isNullOrEmpty()){
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
            return
        }

        if(Locker.hasInfinityLocked()){
            Toast.makeText(this@MainActivity, R.string.infinity_locked, Toast.LENGTH_SHORT).show()
            return
        }
        DialogCheckPassword.Builder(this@MainActivity)
            .setMessage(R.string.enter_the_password)
            .setOnClickYes(R.string.ok) { dlg, password ->
                dlg.dismiss()
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }.setOnClickNo(R.string.no) { dlg ->
                dlg.dismiss()
            }.build()
            .show()
    }

    private fun showMemo(memo: MemoData){
        if(!InstancePassword.getPassword().isNullOrEmpty() || !memo.hasEnc){
            val intent = Intent(this@MainActivity, ViewMemoActivity::class.java)
            intent.putExtra(ViewMemoActivity.INDEX, memo.index)
            intent.putExtra(ViewMemoActivity.PASSWORD, InstancePassword.getPassword())
            startActivity(intent)
            return
        }

        if(Locker.hasInfinityLocked()){
            Toast.makeText(this@MainActivity, R.string.infinity_locked, Toast.LENGTH_SHORT).show()
            return
        }
        DialogCheckPassword.Builder(this@MainActivity)
            .setMessage(R.string.enter_the_password)
            .setOnClickYes(R.string.ok) { dlg, password ->
                dlg.dismiss()
                val intent = Intent(this@MainActivity, ViewMemoActivity::class.java)
                intent.putExtra(ViewMemoActivity.INDEX, memo.index)
                intent.putExtra(ViewMemoActivity.PASSWORD, password)
                startActivity(intent)
            }.setOnClickNo(R.string.no) { dlg ->
                dlg.dismiss()
            }.build()
            .show()
    }

    private fun showInputPassword() {
        DialogInputPassword.Builder(this@MainActivity)
            .setMessage(R.string.enc_input_dialog_message)
            .setOnClickYes(R.string.ok) { dlg, password ->
                savePassword(password)
                dlg.dismiss()
            }.setOnFinished {
                if (MyApplication.prefs.hashKey.isNullOrEmpty()) {
                    this@MainActivity.finish()
                }
            }.build()
            .show()
    }

    private fun savePassword(password: String) {
        val hashKey = BCrypt.hashpw(password, BCrypt.gensalt(10))
        MyApplication.prefs.hashKey = hashKey
    }

    private fun load() {
        viewDataBinding.vmMain?.let { vm ->
            rcvMemoList.adapter = ListAdapterMemo(vm)
            vm.load()?.observe(this, Observer { pagedList ->
                (rcvMemoList.adapter as ListAdapterMemo).submitList(pagedList)
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventMemoUpdate(command: EventMemoUpdate){
//        MyLog.debug(TAG, "eventMemoUpdate()")
        load()
    }

}