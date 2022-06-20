package com.yun.simpledaily.ui.week

import android.app.Application
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.model.WeekWeatherModel

class WeekWeatherViewModel(
    application: Application
) : BaseViewModel(application) {
    val weekWeatherList = ListLiveData<WeekWeatherModel.RS>()
}