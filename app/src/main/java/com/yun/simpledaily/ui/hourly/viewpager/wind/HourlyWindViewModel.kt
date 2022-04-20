package com.yun.simpledaily.ui.hourly.viewpager.wind

import android.app.Application
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.model.HourlyWeatherModel

class HourlyWindViewModel(
    application: Application
) : BaseViewModel(application){

    val hourlyWindList = ListLiveData<HourlyWeatherModel.Weather>()

}