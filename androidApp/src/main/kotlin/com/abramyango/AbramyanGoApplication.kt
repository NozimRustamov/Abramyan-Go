package com.abramyango

import android.app.Application
import com.abramyango.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AbramyanGoApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AbramyanGoApplication)
            modules(
                uiModule
            )
        }
    }
}
