package com.abramyango.ui

import com.abramyango.core.di.platformModule
import com.abramyango.core.di.sharedModule
import com.abramyango.ui.di.uiModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            platformModule,
            sharedModule,
            uiModule
        )
    }
}
