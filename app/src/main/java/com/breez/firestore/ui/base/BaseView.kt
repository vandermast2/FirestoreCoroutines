package com.breez.firestore.ui.base

interface BaseView {
    fun showAlert(text: String? = null)

    fun showProgress()

    fun hideProgress()
}
