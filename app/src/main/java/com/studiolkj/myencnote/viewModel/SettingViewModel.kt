package com.studiolkj.myencnote.viewModel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.studiolkj.myencnote.MyApplication
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.common.*
import com.studiolkj.myencnote.model.InstancePassword
import com.studiolkj.myencnote.model.database.MemoDao
import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.model.database.MemoDatabase
import com.studiolkj.myencnote.model.main.RoomPagingMemoDataSource
import com.studiolkj.myencnote.view.dialog.DialogChangePassword
import com.studiolkj.myencnote.view.dialog.DialogInputPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.mindrot.jbcrypt.BCrypt

class SettingViewModel: BaseViewModel() {

    companion object {
        val TAG = "SettingViewModel"
    }

    val holdingTime: NotNullMutableLiveData<Int> = NotNullMutableLiveData(MyApplication.prefs.holdingTime)
    val retryType: NotNullMutableLiveData<Int> = NotNullMutableLiveData(MyApplication.prefs.retryType)
    val lockType: NotNullMutableLiveData<Int> = NotNullMutableLiveData(MyApplication.prefs.lockType)

    override fun onCleared() {
        super.onCleared()
    }

    fun onSelectHoldingTime(index: Int) {
        holdingTime.postValue(index)
        MyApplication.prefs.holdingTime = index
    }

    fun onSelectRetryType(index: Int) {
        retryType.postValue(index)
        MyApplication.prefs.retryType = index
    }

    fun onSelectLockType(index: Int) {
        lockType.postValue(index)
        MyApplication.prefs.lockType = index
    }


    fun onClickChangePassword(view: View) {
        DialogChangePassword.Builder(view.context)
            .setMessage(R.string.enc_change_dialog_message)
            .setOnClickYes(R.string.ok) { dlg, oldPassword, newPassword ->
                changePassword(view.context, oldPassword, newPassword)
                dlg.dismiss()
                Toast.makeText(view.context, R.string.succssed_change_password, Toast.LENGTH_SHORT).show()
            }.setOnClickNo(R.string.no){
                it.dismiss()
            }.build()
            .show()
    }

    fun changePassword(context: Context, oldPassword: String, newPassword: String) {

        GlobalScope.launch(Dispatchers.IO) {
            val newHashKey = BCrypt.hashpw(newPassword, BCrypt.gensalt(10))
            MyApplication.prefs.hashKey = newHashKey
            val db = MemoDatabase.getInstance(context).getDB()
            val memoList = db.getAll()
            memoList?.let { memos ->
                for(memo in memos){
                    if(memo.hasEnc){
                        memo.encData?.let {
                            val decData = Utils.decData(oldPassword, it)
                            memo.encData = Utils.encData(newPassword, decData)
                            db.updateMemoData(memo)
                        }
                    }
                }
            }
        }
    }

}