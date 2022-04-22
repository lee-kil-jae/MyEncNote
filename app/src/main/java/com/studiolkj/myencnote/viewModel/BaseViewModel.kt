package com.studiolkj.myencnote.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    companion object{

    }

    val back: MutableLiveData<Boolean> = MutableLiveData()
    val toastMessage: MutableLiveData<Int> = MutableLiveData()
    val toastMessage2: MutableLiveData<String> = MutableLiveData()

    open fun onBack(view : View){
        back.postValue(true)
    }

    /**
     * RxJava Observing을 위한 부분.
     * addDisposable을 이용하여 추가하기만 하면 된다.
     */

    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable : Disposable){
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}