package com.breez.firestore.dataFlow

import com.breez.firestore.model.AnnounceModel
import com.breez.firestore.util.addAwait
import com.breez.firestore.util.await
import com.breez.firestore.util.deleteAwait
import com.breez.firestore.util.updateAwait
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class DataManger @Inject constructor(val firestoreDB: FirebaseFirestore, val collectionReference: CollectionReference) :
    IDataManager {
    override suspend fun update(id: String, map: HashMap<String, Any>) {
        collectionReference.document(id).updateAwait(map)
    }

    override suspend fun delete(id: String) {
        collectionReference.document(id).deleteAwait()
    }

    override suspend fun getByID(id: String): AnnounceModel? = getAll().find { it.id == id }

    override suspend fun add(item: HashMap<String, Any>) {
        collectionReference.addAwait(item)
    }

    override suspend fun getAll() = collectionReference.await(AnnounceModel::class.java)
}
