package com.github.crisacm.sessionmanager

import android.app.Application
import com.github.crisacm.sessionmanager.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SessionApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SessionApplication)
            modules(appModules)
        }
    }
}