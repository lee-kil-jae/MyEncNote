package com.studiolkj.myencnote.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.studiolkj.myencnote.MyApplication
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.model.InstancePassword
import kotlinx.android.synthetic.main.dialog_changepassword.view.*
import org.mindrot.jbcrypt.BCrypt

class DialogChangePassword {
    data class Builder(
        var context: Context? = null,
        var messageId: Int = -1,
        var noId: Int = -1,
        var yesId: Int = -1,
        var onFinished: (()->Unit)? = null,
        var onClickNo: ((AlertDialog)->Unit)? = null,
        var onClickYes: ((AlertDialog, String, String)->Unit)? = null
    ){
        lateinit var dialog: AlertDialog
        fun context(context: Context) = apply { this.context = context }
        fun setMessage(stringId: Int) = apply { this.messageId = stringId }
        fun setOnFinished(onFinished: (() -> Unit)) = apply { this.onFinished = onFinished }
        fun setOnClickNo(stringId: Int, onClickNo: ((AlertDialog) -> Unit)) = apply {
            this.noId = stringId
            this.onClickNo = onClickNo
        }
        fun setOnClickYes(stringId: Int, onClickYes: ((AlertDialog, String, String) -> Unit)) = apply {
            this.yesId = stringId
            this.onClickYes = onClickYes
        }
        fun build(): AlertDialog {
            context?.run {
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView = inflater.inflate(R.layout.dialog_changepassword, null)
                dialog = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create()
                dialog?.window?.let {
                    val windowLayoutParam = it.attributes
                    windowLayoutParam.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
                    it.attributes = windowLayoutParam
                    it.setBackgroundDrawableResource(R.drawable.bg_round10_white)
                }

                if(messageId <= 0){
                    dialogView.txtDesc.visibility = View.GONE
                }else {
                    dialogView.txtDesc.visibility = View.VISIBLE
                    dialogView.txtDesc.text = getString(messageId)
                }

                if(noId <= 0){
                    dialogView.btnNo.visibility = View.GONE
                }else {
                    dialogView.btnNo.visibility = View.VISIBLE
                    dialogView.btnNo.text = getString(noId)
                    dialogView.btnNo.setOnClickListener {
                        onClickNo?.invoke(dialog)
                    }
                }

                if(yesId <= 0){
                    dialogView.btnYes.visibility = View.GONE
                }else {
                    dialogView.btnYes.visibility = View.VISIBLE
                    dialogView.btnYes.text = getString(yesId)
                    dialogView.btnYes.setOnClickListener {
                        if(hasCorrectEncKey(dialogView)) {
                            val newPassword = dialogView.edtNewEncKey.text.toString().trim()
                            InstancePassword.setPassword(newPassword)
                            onClickYes?.invoke(dialog, dialogView.edtCurrentEncKey.text.toString(), newPassword)
                        }
                    }
                }

                dialog?.setOnDismissListener {
                    onFinished?.invoke()
                }
            }
            return dialog
        }

        private fun hasCorrectEncKey(dialogView: View): Boolean {
            val currentEncKey = dialogView.edtCurrentEncKey.text.toString().trim()
            val newEncKey = dialogView.edtNewEncKey.text.toString().trim()
            val newEncKeyRe = dialogView.edtNewEncKeyRe.text.toString().trim()

            if (currentEncKey.isNullOrEmpty() || currentEncKey.length < 4 || newEncKey.isNullOrEmpty() || newEncKey.length < 4) {
                Toast.makeText(dialogView.context, R.string.correct_password1, Toast.LENGTH_SHORT).show()
                return false
            }

            if (!BCrypt.checkpw(currentEncKey, MyApplication.prefs.hashKey)){
                Toast.makeText(dialogView.context, R.string.correct_password3, Toast.LENGTH_SHORT).show()
                return false
            }

            if (!newEncKey.equals(newEncKeyRe)){
                Toast.makeText(dialogView.context, R.string.correct_password2, Toast.LENGTH_SHORT).show()
                return false
            }

            return true
        }
    }
}