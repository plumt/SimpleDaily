package com.yun.simpledaily.data.model

import android.os.Parcelable
import com.yun.simpledaily.base.Item
import kotlinx.android.parcel.Parcelize

class WeekWeatherModel {
    @Parcelize
    data class RS(
        override var id: Int,
        override var viewType: Int,
        val dayOfWeek: String,
        val date: String,
        val amPercent: String,
        val pmPercent: String,
        val lowTemp: String,
        val highTemp: String,
        val amImg: String,
        val pmImg: String
    ) : Item(), Parcelable
}