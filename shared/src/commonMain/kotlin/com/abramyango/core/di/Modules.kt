package com.abramyango.core.di

import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule = module {
    // CategoryRepository is registered in uiModule (composeApp)
}

expect val platformModule: Module
