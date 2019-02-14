package com.breez.firestore.di

import android.app.Application
import android.content.Context
import com.breez.firestore.dataFlow.DataManger
import com.breez.firestore.dataFlow.IDataManager
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@InternalCoroutinesApi
@Module(includes = [FirebaseModule::class])
class AppModule(private val application: Application) {
    init {
        FirebaseApp.initializeApp(application)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application


    @Provides
    @Singleton
    fun provideDataManager(
        firebaseFirestore: FirebaseFirestore,
        collectionReference: CollectionReference
    ): IDataManager = DataManger(firebaseFirestore, collectionReference)
}