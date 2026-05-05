package tj.abramyan.go

import androidx.compose.ui.window.ComposeUIViewController
import tj.abramyan.go.ui.App
import tj.abramyan.go.ui.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

fun MainViewController() = ComposeUIViewController { App() }
