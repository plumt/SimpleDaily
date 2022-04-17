package com.yun.simpledaily.di

import com.yun.simpledaily.ui.calendar.CalendarViewModel
import com.yun.simpledaily.ui.home.HomeViewModel
import com.yun.simpledaily.ui.location.LocationViewModel
import com.yun.simpledaily.ui.main.MainViewModel
import com.yun.simpledaily.ui.memo.MemoViewModel
import com.yun.simpledaily.ui.setting.SettingViewModel
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

    viewModel {
        CalendarViewModel(get())
    }

    viewModel {
        MemoViewModel(get(), get())
    }

    viewModel {
        SettingViewModel(get())
    }

    viewModel {
        LocationViewModel(get(), get(named("location")))
    }
}

