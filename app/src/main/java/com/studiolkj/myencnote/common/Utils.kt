package com.studiolkj.myencnote.common


import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.studiolkj.myencnote.R
import java.security.DigestException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Utils {

    var randomIndex = 0
    @BindingAdapter("randomDrawable")
    @JvmStatic
    fun randomDrawable(view : View, hasRandom: Boolean){
        when(randomIndex%7){
            0 -> view.setBackgroundResource(R.drawable.bg_round10_banana)
            1 -> view.setBackgroundResource(R.drawable.bg_round10_greenapple)
            2 -> view.setBackgroundResource(R.drawable.bg_round10_pear)
            3 -> view.setBackgroundResource(R.drawable.bg_round10_pear2)
            4 -> view.setBackgroundResource(R.drawable.bg_round10_pear3)
            5 -> view.setBackgroundResource(R.drawable.bg_round10_crayolapeach)
            else -> view.setBackgroundResource(R.drawable.bg_round10_vanillacream)
        }
        randomIndex += 1
    }

    @JvmStatic
    fun getLockString(value: Boolean): Int {
        return if (value) { R.string.this_memo_lock_save } else { R.string.this_memo_unlock_save }
    }

    @BindingAdapter("encDrawable")
    @JvmStatic
    fun lockDrawable(view : ImageView, hasEnc: Boolean){
        if(hasEnc) {
            view.setImageResource(R.drawable.ic_addmemo_lock)
        }else{
            view.setImageResource(R.drawable.ic_addmemo_unlock)
        }
    }

    @JvmStatic
    fun falseIsVisible(value: Boolean): Int {
        return if (value) { View.GONE } else { View.VISIBLE }
    }

    @JvmStatic
    fun trueIsVisible(value: Boolean): Int {
        return if (value) { View.VISIBLE } else { View.GONE }
    }

    @JvmStatic
    fun editedAtString(time: Long): String{
        return if(hasKorean()) {
            SimpleDateFormat("yyyy.M.d a h:m").format(Date(time))
        }else{
            SimpleDateFormat("MM/dd/yy a h:m").format(Date(time))
        }
    }

    @JvmStatic
    fun hasKorean(): Boolean{
        return Locale.getDefault().language == "ko"
    }

    @JvmStatic
    fun decData(encKey: String, encData: ByteArray): String {
        var iv = ByteArray(16)
        val keySpec = SecretKeySpec(hashSHA256(encKey), "AES")
        val cipher_dec = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher_dec.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
        val byteDecryptedText = cipher_dec.doFinal(encData)
        return String(byteDecryptedText)
    }

    @JvmStatic
    fun encData(encKey: String, data: String): ByteArray {
        var iv = ByteArray(16)
        val keySpec = SecretKeySpec(hashSHA256(encKey), "AES")
        val cipher_enc = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher_enc.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
        return cipher_enc.doFinal(data.toByteArray())
    }

    @JvmStatic
    private fun hashSHA256(msg: String): ByteArray {
        val hash: ByteArray
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(msg.toByteArray())
            hash = md.digest()
        } catch (e: CloneNotSupportedException) {
            throw DigestException("couldn't make digest of partial content")
        }
        return hash
    }
    @JvmStatic
    fun createCryptoKey(): String {
        val ALLOWED_CHARACTERS = "qw01erty23uiop45as6dfghj7klzxc89vbnm"
        val random = Random()
        val sb = StringBuilder(16)
        for (i in 0 until 16)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }
}