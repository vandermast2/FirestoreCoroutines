package com.breez.firestore.ui.activities

import android.content.ContentValues
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.breez.firestore.R
import com.breez.firestore.model.AnnounceModel
import com.breez.firestore.ui.adapters.ListAdapter
import com.breez.firestore.ui.base.BaseActivity
import com.breez.firestore.ui.base.OnItemClickedListener
import com.breez.firestore.ui.fragments.detail.DetailFragment
import com.breez.firestore.ui.fragments.dialogs.CustomDialogFragment
import com.breez.firestore.util.Constants
import com.breez.firestore.util.onClick
import com.breez.firestore.util.replaceFragmentSafely
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
class MainActivity : BaseActivity<MainViewModel>() {
    private lateinit var adapter: ListAdapter
    override val layoutId: Int = R.layout.activity_main
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
    override val containerId: Int = R.id.container
    override val observeLiveData: MainViewModel.() -> Unit = {
        getAnnounces().observe(this@MainActivity, Observer { listResource ->
            adapter.setList(listResource)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecycler()
        btnAdd.onClick { openDialog(null, Constants.EDIT_TYPE) }
    }

    private fun initRecycler() {
        adapter = ListAdapter(listener = object : OnItemClickedListener<AnnounceModel> {
            override fun onClicked(item: AnnounceModel, type: String) {
                when (type) {
                    Constants.CLICK_TYPE -> replaceFragmentSafely(
                        DetailFragment.newInstance(item.id),
                        Constants.DETAIL_FRAGMENT_TAG,
                        false,
                        true,
                        containerId
                    )
                    Constants.EDIT_TYPE -> {
                        openDialog(item.id, type)
                    }
                    Constants.DELETE_TYPE -> {
                        openDialog(item.id, type)
                    }
                }
            }
        })
        recyxler.layoutManager = LinearLayoutManager(this)
        recyxler.adapter = adapter
    }

    private fun openDialog(id: String?, type: String) {
        CustomDialogFragment.newInstance(id, type).show(supportFragmentManager.beginTransaction(), ContentValues.TAG)
    }

}
