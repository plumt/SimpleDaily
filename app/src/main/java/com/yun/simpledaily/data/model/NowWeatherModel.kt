package com.yun.simpledaily.data.model

class NowWeatherModel {
    data class Weather(
        val imagePath: String,
        val location: String,
        val temperature: String,
        val weather: String,
        val compare: String,
        val weatherDetail: String
//        val dust: String,
//        val uDust: String,
//        val uv: String
    )
}