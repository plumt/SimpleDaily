package com.yun.simpledaily.di

import com.yun.simpledaily.ui.home.HomeViewModel
import com.yun.simpledaily.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(get(), get())
    }


    viewModel {
        HomeViewModel(get(), get(named("signal")))
    }
}

