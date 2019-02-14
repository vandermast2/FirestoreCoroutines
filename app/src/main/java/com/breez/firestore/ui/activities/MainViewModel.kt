package com.breez.firestore.ui.activities

import androidx.lifecycle.MutableLiveData
import com.breez.firestore.model.AnnounceModel
import com.breez.firestore.ui.base.BaseVM
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class MainViewModel : BaseVM() {
    private val announces: MutableLiveData<List<AnnounceModel>> = object : MutableLiveData<List<AnnounceModel>>() {
        override fun onActive() {
            super.onActive()
            GlobalScope.launch { getall() }
        }
    }

    fun getAnnounces() = announces

    suspend fun onResume() {
        getall()
    }

    suspend fun add(item: HashMap<String, Any>) {
        dataManager.add(item)
    }

    private suspend fun getall() {
        announces.postValue(dataManager.getAll())
    }

    suspend fun getByID(id: String) = dataManager.getByID(id)

    suspend fun delete(id: String) {
        dataManager.delete(id)
        getall()
    }

    suspend fun addItem(map: HashMap<String, Any>) {
        dataManager.add(map)
        getall()
    }

    suspend fun update(id: String, map: HashMap<String, Any>) {
        dataManager.update(id, map)
        getall()
    }
}
