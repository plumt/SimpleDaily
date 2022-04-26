package com.yun.simpledaily.di

import com.yun.simpledaily.ui.board.BoardSettingViewModel
import com.yun.simpledaily.ui.home.HomeViewModel
import com.yun.simpledaily.ui.hourly.HourlyWeatherViewModel
import com.yun.simpledaily.ui.location.LocationViewModel
import com.yun.simpledaily.ui.main.MainViewModel
import com.yun.simpledaily.ui.memo.MemoViewModel
import com.yun.simpledaily.ui.memo.viewpager.detail.MemoDetailViewModel
import com.yun.simpledaily.ui.memo.viewpager.list.MemoListViewModel
import com.yun.simpledaily.ui.memo.viewpager.write.MemoWriteViewModel
import com.yun.simpledaily.ui.news.NaverNewsViewModel
import com.yun.simpledaily.ui.schedule.ScheduleViewModel
import com.yun.simpledaily.ui.schedule.viewpager.calendar.CalendarViewModel
import com.yun.simpledaily.ui.schedule.viewpager.list.ScheduleListViewModel
import com.yun.simpledaily.ui.setting.SettingViewModel
import com.yun.simpledaily.ui.week.WeekWeatherViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(get(), get())
    }

    viewModel {
        HomeViewModel(get(), get(named("signal")), get(), get())
    }

    viewModel {
        ScheduleViewModel(get())
    }

    viewModel {
        MemoViewModel(get(), get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }

    viewModel {
        LocationViewModel(get(), get(named("location")))
    }

    viewModel {
        MemoListViewModel(get())
    }

    viewModel {
        MemoDetailViewModel(get(), get())
    }

    viewModel {
        MemoWriteViewModel(get(), get())
    }

    viewModel {
        BoardSettingViewModel(get(), get())
    }

    viewModel {
        HourlyWeatherViewModel(get())
    }

    viewModel {
        CalendarViewModel(get(), get())
    }
    viewModel {
        ScheduleListViewModel(get())
    }
    viewModel {
        WeekWeatherViewModel(get())
    }
    viewModel {
        NaverNewsViewModel(get())
    }
}

