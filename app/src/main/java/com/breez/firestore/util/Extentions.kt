package com.breez.firestore.util

import android.view.View


fun View.onClick(body: () -> Unit) {
    setOnClickListener { body() }
}