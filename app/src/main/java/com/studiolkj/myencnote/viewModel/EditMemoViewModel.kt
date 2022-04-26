package com.studiolkj.myencnote.viewModel

import android.view.View
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.common.EventMemoUpdate
import com.studiolkj.myencnote.common.NotNullMutableLiveData
import com.studiolkj.myencnote.common.Utils
import com.studiolkj.myencnote.model.database.MemoDao
import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.view.dialog.DialogCheckPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class EditMemoViewModel(val index: Int, val password: String, val db: MemoDao): BaseViewModel() {

    companion object {
        val TAG = "EditMemoViewModel"
    }

    val hasEnc: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val hintString: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val dataString: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    lateinit var memo: MemoData

    init{
        GlobalScope.launch(Dispatchers.IO) {
            memo = db.getMemoData(index)
            hintString.postValue(memo.hint)
            hasEnc.postValue(memo.hasEnc)
            if(memo.hasEnc) {
                memo.encData?.let {
                    dataString.postValue(Utils.decData(password, it))
                }
            } else {
                dataString.postValue(memo.openData)
            }
        }
    }

    fun onClickEnc(view: View) {
        if (hasEnc.value) {
            hasEnc.postValue(false)
        } else {
            hasEnc.postValue(true)
        }
    }

    fun onClickRemove(view: View) {
        DialogCheckPassword.Builder(view.context)
            .setMessage(R.string.enter_remove_password)
            .setOnClickYes(R.string.ok) { dlg, password ->
                removeMemo()
                dlg.dismiss()
            }.setOnClickNo(R.string.no) { dlg ->
                dlg.dismiss()
            }.build()
            .show()
    }

    fun onClickUpdate(view: View) {
        var hintString = hintString.value
        if(hintString.isNullOrEmpty()) {
            hintString = view.context.getString(R.string.empty_hint)
        }

        val dataString = dataString.value
        if(dataString.isNullOrEmpty()){
            toastMessage.postValue(R.string.empty_data_plz_enter_a_data)
            return
        }

        if (!hasEnc.value) {
            updateMemo(false, hintString, dataString, "")
        } else {
            DialogCheckPassword.Builder(view.context)
                .setMessage(R.string.enter_the_password)
                .setOnClickYes(R.string.ok) { dlg, password ->
                    updateMemo(true, hintString, dataString, password)
                    dlg.dismiss()
                }.setOnClickNo(R.string.no) { dlg ->
                    dlg.dismiss()
                }.build()
                .show()
        }
    }

    private fun removeMemo() {
        GlobalScope.launch(Dispatchers.IO) {
            db.deleteMemo(index)
            EventBus.getDefault().post(EventMemoUpdate(true))
            toastMessage.postValue(R.string.remove_memo)
            back.postValue(true)
        }
    }

    private fun updateMemo(hasEncrypt: Boolean, hint: String, data: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            memo.hasEnc = hasEncrypt
            memo.hint = hint
            memo.editedAt = System.currentTimeMillis()
            if (hasEncrypt) {
                memo.openData = ""
                memo.encData = Utils.encData(password, data)
            } else {
                memo.openData = data
                memo.encData = null
            }
            db.updateMemoData(memo)

            EventBus.getDefault().post(EventMemoUpdate(true))
            toastMessage.postValue(R.string.update_memo)
            back.postValue(true)
        }
    }
}