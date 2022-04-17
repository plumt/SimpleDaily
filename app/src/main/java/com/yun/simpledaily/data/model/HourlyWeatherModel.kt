package com.yun.simpledaily.data.model

import com.yun.simpledaily.base.Item

class HourlyWeatherModel {
    data class RS(
        override var id: Int,
        override var viewType: Int = 0,
        val time: String,
        val num: String,
        val wea: String,
        val url: String
    ) : Item()
}