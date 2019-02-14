package com.breez.firestore.dataFlow

import com.breez.firestore.model.AnnounceModel

interface IDataManager {
    suspend fun getAll(): List<AnnounceModel>
    suspend fun add(item: HashMap<String, Any>)
    suspend fun getByID(id: String): AnnounceModel?
    suspend fun delete(id: String)
    suspend fun update(id: String, map: HashMap<String, Any>)
}
