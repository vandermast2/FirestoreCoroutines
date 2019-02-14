package com.breez.firestore.ui.base

import com.breez.firestore.model.AnnounceModel

interface OnItemClickedListener<T> {
    fun onClicked(item: AnnounceModel, type: String)
}
