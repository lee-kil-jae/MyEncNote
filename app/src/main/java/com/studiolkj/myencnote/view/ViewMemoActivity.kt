package com.studiolkj.myencnote.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.common.EventMemoUpdate
import com.studiolkj.myencnote.common.Utils
import com.studiolkj.myencnote.databinding.ActivityViewmemoBinding
import com.studiolkj.myencnote.model.database.MemoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class ViewMemoActivity: BaseActivity<ActivityViewmemoBinding>() {
    companion object {
        val TAG = "ViewMemoActivity"
        val INDEX = "index"
        val PASSWORD = "password"
    }

    override val layoutResourceId: Int get() = R.layout.activity_viewmemo
    var index = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = intent.getIntExtra(INDEX, -1)
        val password = intent.getStringExtra(PASSWORD)?: ""

        if(index == -1){
            finish()
            return
        }

        viewDataBinding.vmViewMemo = getViewModel { parametersOf(index, password) }
        viewDataBinding.lifecycleOwner = this@ViewMemoActivity

        viewDataBinding.vmViewMemo?.editMemo?.observe(this, Observer {
            val intent = Intent(this@ViewMemoActivity, EditMemoActivity::class.java)
            intent.putExtra(EditMemoActivity.INDEX, index)
            intent.putExtra(EditMemoActivity.PASSWORD, password)
            startActivity(intent)
        })

        initObserve(viewDataBinding.vmViewMemo)

        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventMemoUpdate(command: EventMemoUpdate){
        viewDataBinding.vmViewMemo?.load()
    }
}