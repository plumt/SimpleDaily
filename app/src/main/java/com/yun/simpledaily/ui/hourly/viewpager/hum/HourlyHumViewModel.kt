package com.yun.simpledaily.ui.hourly.viewpager.hum

import android.app.Application
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.model.HourlyWeatherModel

class HourlyHumViewModel(
    application: Application
) : BaseViewModel(application){

    val hourlyHumList = ListLiveData<HourlyWeatherModel.Weather>()

}