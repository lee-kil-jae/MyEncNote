package com.studiolkj.myencnote.view

import android.os.Bundle
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.databinding.ActivityEditmemoBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class EditMemoActivity: BaseActivity<ActivityEditmemoBinding>() {
    companion object {
        val TAG = "EditMemoActivity"
        val INDEX = "index"
        val PASSWORD = "password"
    }

    override val layoutResourceId: Int get() = R.layout.activity_editmemo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = intent.getIntExtra(INDEX, -1)
        val password = intent.getStringExtra(PASSWORD)?: ""

        if(index == -1){
            finish()
            return
        }

        viewDataBinding.vmEditMemo = getViewModel { parametersOf(index, password) }
        viewDataBinding.lifecycleOwner = this@EditMemoActivity

        initObserve(viewDataBinding.vmEditMemo)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}