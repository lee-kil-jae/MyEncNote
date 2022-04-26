package com.studiolkj.myencnote.viewModel

import android.view.View
import com.studiolkj.myencnote.MyApplication
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.common.EventMemoUpdate
import com.studiolkj.myencnote.common.NotNullMutableLiveData
import com.studiolkj.myencnote.common.Utils
import com.studiolkj.myencnote.model.database.MemoDao
import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.view.dialog.DialogCheckPassword
import com.studiolkj.myencnote.view.dialog.DialogInputPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class AddMemoViewModel(val db: MemoDao): BaseViewModel() {

    companion object {
        val TAG = "AddMemoViewModel"
    }

    val hasEnc: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val hint: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val data: NotNullMutableLiveData<String> = NotNullMutableLiveData("")

    fun onClickEnc(view: View) {
        if (hasEnc.value) {
            hasEnc.postValue(false)
        } else {
            hasEnc.postValue(true)
        }
    }

    fun onClickSave(view: View) {
        var hintString = hint.value
        if(hintString.isNullOrEmpty()) {
            hintString = view.context.getString(R.string.empty_hint)
        }

        val dataString = data.value
        if(dataString.isNullOrEmpty()){
            toastMessage.postValue(R.string.empty_data_plz_enter_a_data)
            return
        }

        if (!hasEnc.value) {
            saveMemo(false, hintString, dataString, "")
        } else {
            DialogCheckPassword.Builder(view.context)
                .setMessage(R.string.enter_the_password)
                .setOnClickYes(R.string.ok) { dlg, password ->
                    saveMemo(true, hintString, dataString, password)
                    dlg.dismiss()
                }.setOnClickNo(R.string.no) { dlg ->
                    dlg.dismiss()
                }.build()
                .show()
        }
    }

    private fun saveMemo(hasEncrypt: Boolean, hint: String, data: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            if (hasEncrypt) {
                val memo = MemoData(hasEnc.value, hint, System.currentTimeMillis(), "", Utils.encData(password, data))
                db.insertMemoData(memo)
            } else {
                val memo = MemoData(hasEnc.value, hint, System.currentTimeMillis(), data, null)
                db.insertMemoData(memo)
            }

            EventBus.getDefault().post(EventMemoUpdate(true))
            toastMessage.postValue(R.string.save_memo)
            back.postValue(true)
        }
    }
}