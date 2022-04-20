package com.yun.simpledaily.ui.hourly

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.model.HourlyWeatherModel

class HourlyWeatherViewModel(
    application: Application
) : BaseViewModel(application){

    val hourlyRainList = ListLiveData<HourlyWeatherModel.Weather>()
    val hourlyWindList = ListLiveData<HourlyWeatherModel.Weather>()
    val hourlyHumList = ListLiveData<HourlyWeatherModel.Weather>()


    val title = MutableLiveData<String>().apply {
        value = mContext.getString(R.string.hourly_more_txt)
    }
}