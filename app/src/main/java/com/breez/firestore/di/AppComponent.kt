package com.breez.firestore.di

import com.breez.firestore.AppApplication
import com.breez.firestore.ui.base.BaseVM
import dagger.Component
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@InternalCoroutinesApi
@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(appApplication: AppApplication)
    fun inject(baseVM: BaseVM)
}