package com.studiolkj.myencnote.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.studiolkj.myencnote.common.NotNullMutableLiveData
import com.studiolkj.myencnote.common.Utils
import com.studiolkj.myencnote.model.database.MemoDao
import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.model.database.MemoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class ViewMemoViewModel(val index: Int, val password: String, val db: MemoDao): BaseViewModel() {

    companion object {
        val TAG = "ViewMemoViewModel"
    }

    val hasEnc: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val hintString: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val dataString: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val editMemo: MutableLiveData<Boolean> = MutableLiveData()

    init {
        load()
    }

    fun load() {
        GlobalScope.launch(Dispatchers.IO) {
            val memo = db.getMemoData(index)
            if (memo == null) {
                back.postValue(true)
            } else {
                hasEnc.postValue(memo.hasEnc)
                hintString.postValue(memo.hint)
                if (memo.hasEnc) {
                    memo.encData?.let {
                        dataString.postValue(Utils.decData(password, it))
                    }
                } else {
                    dataString.postValue(memo.openData)
                }
            }
        }
    }

    fun onClickEdit(view: View) {
        editMemo.postValue(true)
    }

}