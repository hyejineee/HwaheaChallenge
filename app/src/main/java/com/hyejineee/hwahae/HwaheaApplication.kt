package com.hyejineee.hwahae

import android.app.Application
import com.hyejineee.hwahae.util.mModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HwaheaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HwaheaApplication)
            modules(mModules)
        }
    }
}
