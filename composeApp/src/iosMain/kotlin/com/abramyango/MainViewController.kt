package com.abramyango

import androidx.compose.ui.window.ComposeUIViewController
import com.abramyango.ui.App
import com.abramyango.ui.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

fun MainViewController() = ComposeUIViewController { App() }
