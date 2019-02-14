package com.breez.firestore.ui.base

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.breez.firestore.AppApplication
import com.breez.firestore.dataFlow.IDataManager
import kotlinx.coroutines.*
import javax.inject.Inject

@InternalCoroutinesApi
open class BaseVM : ViewModel() {
    @Inject
    lateinit var dataManager: IDataManager
    val progressLiveData = MutableLiveData<Boolean>()
    val alertMessage = MutableLiveData<String?>()
    val uriLiveData = MutableLiveData<Uri>()
    protected val tag: String = javaClass.simpleName
    private val coroutines = mutableListOf<Deferred<*>>()
    private val subscriptions = mutableListOf<Job>()

    init {
        AppApplication.component.inject(this)
    }

    protected fun addCoroutine(coroutine: Deferred<*>): Deferred<*> {
        coroutines.add(coroutine)
        return coroutine
    }

    override fun onCleared() {
        coroutines.forEach { it.cancel() }
        coroutines.clear()
        subscriptions.forEach { it.cancel() }
        subscriptions.clear()
        super.onCleared()
    }

    protected fun addSubscription(job: Job): Job {
        subscriptions.add(job)
        return job
    }

    fun showProgress() {
        progressLiveData.postValue(true)
    }

    fun hideProgress() {
        progressLiveData.postValue(false)
    }

    fun <T : Any?> processAsyncProviderCall(
        call: () -> Deferred<T>,
        onSuccess: (T) -> Unit = { /* nothing by default*/ },
        onError: (E: Throwable?) -> Unit = { /* nothing by default*/ },
        showProgress: Boolean = false
    ): Deferred<*> = processAsyncProviderCallWithFullResult(
        call, { onSuccess(it) },
        onError, showProgress
    )


    @ExperimentalCoroutinesApi
    private fun <T : Any?>
            processAsyncProviderCallWithFullResult(
        call: () -> Deferred<T>,
        onSuccess: (T) -> Unit = { /* nothing by default*/ },
        onError: (E: Throwable?) -> Unit = { /* nothing by default*/ },
        showProgress: Boolean = false
    ): Deferred<*> {
        return addCoroutine(GlobalScope.async(Dispatchers.Default) {
            if (showProgress) {
                showProgress()
            }
            val job = async(Dispatchers.Unconfined) {
                call()
            }
            with(job.await()) {
                try {
                    if (await() == null) {
                        onError(Throwable())
                    } else {
                        onSuccess(await())
                    }
                } catch (t: Throwable) {
                    onError(getCompletionExceptionOrNull())
                }
            }
            if (showProgress) {
                hideProgress()
            }
        })
    }


    protected fun onError(throwable: Throwable?) {
//        Log.e(tag, throwable = throwable!!.message)
        showAlert()
    }

    fun showAlert(text: String? = null) {
        alertMessage.value = text ?: ""
    }

    protected open fun processUriResult(uri: Uri) {
        uriLiveData.value = uri
    }

}
