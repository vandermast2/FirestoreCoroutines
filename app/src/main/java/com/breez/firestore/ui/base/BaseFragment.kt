package com.breez.firestore.ui.base

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.breez.firestore.R
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
abstract class BaseFragment<T : BaseVM> : Fragment(), BaseView {

    companion object {
        private const val COROUTINE_DELAY = 1L
    }

    abstract val viewModelClass: Class<T>

    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(viewModelClass) }

    private var canBeClicked = true

    private var errorDialog: DialogFragment? = null

    private val coroutines = mutableListOf<Deferred<*>>()

    @Suppress("MemberVisibilityCanPrivate")
    protected fun addCoroutine(coroutine: Deferred<*>) {
        coroutines.add(coroutine)
    }

    protected abstract val layoutId: Int

    protected open fun observeBaseLiveData() = with(viewModel) {
        progressLiveData.observe(this@BaseFragment, Observer {
            it?.let { if (it) this@BaseFragment.showProgress() else this@BaseFragment.hideProgress() }
        })
        alertMessage.observe(this@BaseFragment, Observer {
            it?.let {
                this@BaseFragment.showAlert(it)
                alertMessage.value = null
            }
        })
        observeLiveData()
    }

    protected abstract val observeLiveData: T.() -> Unit

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeBaseLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        canBeClicked = true
    }

    override fun onDestroy() {
        coroutines.forEach { it.cancel() }
        coroutines.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        with(viewModel.alertMessage) {
            value?.let {
                this@BaseFragment.showAlert(it)
                value = null
            }
        }
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showAlert(text: String?) {
        //TODO
//        if (errorDialog?.isAdded != true) {
//            errorDialog = text.getNotEmptyOrNull()?.let { MessageDialog.newInstance(it) } ?: MessageDialog.newInstance()
//            errorDialog?.show(childFragmentManager, MessageDialog::class.java.simpleName)
//        }
    }

    protected fun invokeIfCanAccepted(withDebounce: Boolean = false, invoke: () -> Unit) {
        if (canBeClicked) {
            if (withDebounce) debounceClick()
            invoke()
        }
    }

    // This is something like debounce in rxBinding, but better :)
    private fun debounceClick() {
        TODO()
//        addCoroutine(async(UI) {
//            canBeClicked = false
//            async(CommonPool) {
//                delay(COROUTINE_DELAY, TimeUnit.SECONDS)
//            }.await()
//            if (isActive) {
//                canBeClicked = true
//            }
//        })
    }

    protected fun onBackPressed() = invokeIfCanAccepted { activity?.onBackPressed() }


    protected fun showAlertDialogCustomView(
        customView: View,
        canelable: Boolean = false,
        positiveText: String = getString(R.string.ok),
        positiveListener: (dialog: DialogInterface, which: Int) -> Unit
        = { _, _ -> },
        negativeText: String = getString(R.string.cancel),
        negativeListener: (dialog: DialogInterface, which: Int) -> Unit
        = { dialog, _ -> dialog.cancel() }
    ) {
        AlertDialog.Builder(context)
            .setView(customView)
            .setCancelable(canelable)
            .setPositiveButton(positiveText, positiveListener)
            .setNegativeButton(negativeText, negativeListener)
            .create()
            .show()
    }


}