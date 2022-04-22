package com.yun.simpledaily.data.model

import android.os.Parcelable
import com.yun.simpledaily.base.Item
import kotlinx.android.parcel.Parcelize


class HourlyWeatherModel {
    @Parcelize
    data class Weather(
        override var id: Int,
        override var viewType: Int = 0,
        val time: String,
        val num: String,
        val wea: String,
        val url: String,
        val subTitle: String = ""
    ) : Item(), Parcelable
}