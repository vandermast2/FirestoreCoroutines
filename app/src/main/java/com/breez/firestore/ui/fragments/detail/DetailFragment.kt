package com.breez.firestore.ui.fragments.detail

import android.os.Bundle
import com.breez.firestore.R
import com.breez.firestore.ui.base.BaseFragment
import com.breez.firestore.util.MiscellaneousUtils
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class DetailFragment : BaseFragment<DetailViewModel>() {
    override val viewModelClass: Class<DetailViewModel> = DetailViewModel::class.java
    override val layoutId: Int = R.layout.detail_fragment
    override val observeLiveData: DetailViewModel.() -> Unit = {

    }

    companion object {
        private val EXTRA_COLLECTION_ID = MiscellaneousUtils.getExtra("collectionId")
        fun newInstance(collectionId: String?) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_COLLECTION_ID, collectionId ?: "")
            }
        }
    }

    private val collectionId by lazy {
        arguments?.getString(EXTRA_COLLECTION_ID)
            ?: ""
    }


}
