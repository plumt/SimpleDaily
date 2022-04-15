package com.yun.simpledaily.util

import android.app.Application
import com.yun.simpledaily.di.apiModule
import com.yun.simpledaily.di.networkModule
import com.yun.simpledaily.di.sharedPreferences
import com.yun.simpledaily.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SimpleDailyApplication  : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SimpleDailyApplication)
            koin.loadModules(
                listOf(
                    viewModelModule,
                    sharedPreferences,
                    networkModule,
                    apiModule
                )
            )
            koin.createRootScope()
        }
    }
}