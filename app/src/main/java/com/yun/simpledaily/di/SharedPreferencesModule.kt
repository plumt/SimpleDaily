package com.yun.simpledaily.di

import com.yun.simpledaily.util.PreferenceManager
import org.koin.dsl.module

val sharedPreferences = module {
    fun provideSharedPref(): PreferenceManager {
        return PreferenceManager
    }
    single { provideSharedPref() }
}