package com.breez.firestore.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
abstract class BaseActivity<T : BaseVM> : AppCompatActivity() {
    abstract val viewModelClass: Class<T>
    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(viewModelClass) }
    protected abstract val layoutId: Int
    protected abstract val containerId: Int
    protected abstract val observeLiveData: T.() -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        observeBaseLiveData()
    }

    override fun onResume() {
        super.onResume()
        with(viewModel.alertMessage) {
            value?.let {
                value = null
            }
        }
    }

    fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting ?: false
    }

    private fun observeBaseLiveData() = with(viewModel) {
        progressLiveData.observe(this@BaseActivity, Observer {
            it?.let { if (it) showProgress() else hideProgress() }
        })
        alertMessage.observe(this@BaseActivity, Observer {
            it?.let {
                alertMessage.value = null
            }
        })

        observeLiveData()
    }

}