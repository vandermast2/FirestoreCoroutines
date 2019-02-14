package com.breez.firestore

import android.app.Application
import com.breez.firestore.di.AppComponent
import com.breez.firestore.di.AppModule
import com.breez.firestore.di.DaggerAppComponent
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

class AppApplication : Application() {
    companion object {
        //platformStatic allow access it from java code
        @InternalCoroutinesApi
        @JvmStatic
        lateinit var component: AppComponent
    }

    @InternalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        component.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + "::Line:" + element.lineNumber + "::" + element.methodName + "()"
                }
            })
        }
    }
}