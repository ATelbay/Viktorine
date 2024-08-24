package com.ara.viktorine

import android.app.Application
import com.ara.viktorine.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ViktorineApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() = startKoin {
        androidLogger()
        androidContext(this@ViktorineApp)
        androidFileProperties()
        modules(appModule)
    }
}