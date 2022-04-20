package com.yun.simpledaily.ui.hourly.viewpager.rain

import android.app.Application
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.model.HourlyWeatherModel

class HourlyRainViewModel(
    application: Application
) : BaseViewModel(application) {

    val hourlyRainList = ListLiveData<HourlyWeatherModel.Weather>()

}