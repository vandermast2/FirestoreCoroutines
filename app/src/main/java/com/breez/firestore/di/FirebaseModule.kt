package com.breez.firestore.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun providesFirestore(): FirebaseFirestore {
        FirebaseFirestore.setLoggingEnabled(true)
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun providesRestaurants(): CollectionReference {
        FirebaseFirestore.setLoggingEnabled(true)
        return FirebaseFirestore.getInstance().collection("announcements")
    }
}
