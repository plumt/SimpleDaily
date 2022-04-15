package com.yun.simpledaily.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.model.RealTimeModel
import com.yun.simpledaily.data.repository.api.Api
import com.yun.simpledaily.util.PreferenceManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.jsoup.Jsoup
import org.koin.core.qualifier.named
import java.lang.Exception

class HomeViewModel(
    application: Application
) : BaseViewModel(application) {

    init {
    }

    fun realtime() {
        // 시그널 실시간 검색어
        // api : https://api.signal.bz/news/realtime
        // 사이트 : https://www.signal.bz/

        val api = Api.realtime()
        viewModelScope.launch {
            try {
                (callApi().realtime()) as RealTimeModel.RS).run {
                    Log.d(TAG,"result : ${this.top10}")
                }
            } catch (e: Exception){
                Log.e(TAG,"${e.printStackTrace()}")
            }
        }
    }

    fun weather() {
        val url =
            "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=서울날씨"

        viewModelScope.async {
            try {

                launch(newSingleThreadContext("weather")) {

                    // 전체 데이터
                    val doc = Jsoup.connect(url).get()

                    // 검새어 (ex - 서울날씨, 부산날씨
                    val location = doc.select(".title_area._area_panel .title")

                    // 현재 온도
                    val temperature = doc.select(".temperature_text")

                    // 현재 날씨
                    val weather = doc.select(".weather_main")

                    // 어제보다 높거나 낮은 정도
                    val tempinfo = doc.select(".temperature_info .temperature.up")

                    // 현재 강수, 습도, 바람 (ex - 강수확률 0% 습도 25% 바람(북서풍) 2m/s
                    val summary_list = doc.select(".temperature_info .summary_list")


                    // 미세먼지, 초미세먼지
                    val dust = doc.select(".item_today.level2")

                    // 자외선
                    val sun = doc.select(".item_today.level3")

                    // 시간별 정보 all
                    val hour_data =
                        doc.select(".forecast_wrap._selectable_tab .hourly_forecast._tab_content")

                    // 시간별 날씨 디테일
                    val hour_wea_time = hour_data[0].select(".weather_graph_box .time")
                    val hour_wea_num = hour_data[0].select(".weather_graph_box .num")
                    val hour_wea_wea = hour_data[0].select(".weather_graph_box .blind")

                    // 시간별 강수 디테일
                    val hour_rain_time = hour_data[1].select(".precipitation_graph_box .text")
                    val hour_rain_num = hour_data[1].select(".precipitation_graph_box .data_inner")
                    val hour_rain_per = hour_data[1].select(".precipitation_graph_box .value")

                    // 시간별 바람 디테일
                    val hour_wind_time = hour_data[2].select(".wind_graph_box .time")
                    val hour_wind_num = hour_data[2].select(".wind_graph_box .num")
                    val hour_wind_way = hour_data[2].select(".wind_graph_box .icon_wrap .data")

                    // 시간별 습도 디테일
                    val hour_hum_time = hour_data[3].select(".climate_box .time_wrap .text")
                    val hour_hum_num = hour_data[3].select(".climate_box .graph_wrap .num")

                    // 주간 예보
                    val week = doc.select(".weekly_forecast_area._toggle_panel .week_item")

                    // 주간 예보 디테일 - 요일
                    val week_weekend = week[0].select(".cell_date .day")
                    // 주간 예보 디테일 - 날짜
                    val week_date = week[0].select(".cell_date .date")
                    // 주간 예보 디테일 - 오전 강수 확률
                    val week_am_rain =
                        week[0].select(".cell_weather .weather_left")[0].select(".rainfall")
                    // 주간 예보 디테일 - 오후 강수 확률
                    val week_pm_rain =
                        week[0].select(".cell_weather .weather_left")[1].select(".rainfall")
                    // 주간 예보 디테일 - 최저기온
                    val week_low_temp = week[0].select(".cell_temperature .lowest")
                    // 주간 예보 디테일 - 최고기온
                    val week_high_temp = week[0].select(".cell_temperature .highest")


                    if (temperature.isNotEmpty() && weather.isNotEmpty() && tempinfo.isNotEmpty()) {
                        val local = location[0].text()
                        val tem = temperature[0].text()
                        val wea = weather[0].text()
                        val info = tempinfo[0].text()
                        val list = summary_list[0].text()
                        val dus1 = dust[0].text()
                        val dus2 = dust[1].text()
                        val sun = sun[0].text()

                        Log.d("lys", "$local / $tem / $info / $wea / $list / $dus1 / $dus2 / $sun")

                        Log.d(
                            "lys",
                            "시간별 날씨 : ${hour_wea_time[0].text()} ${hour_wea_num[0].text()}  ${hour_wea_wea[0].text()}   size:${hour_wea_time.size} / ${hour_wea_num.size} / ${hour_wea_wea.size}"
                        )
                        Log.d(
                            "lys",
                            "시간반 강수 : ${hour_rain_time[0].text()} ${hour_rain_num[0].text()}  ${hour_rain_per[0].text()}   size:${hour_rain_time.size} / ${hour_rain_num.size} / ${hour_rain_per.size}"
                        )
                        Log.d(
                            "lys",
                            "시간별 바람 : ${hour_wind_time[0].text()} ${hour_wind_num[0].text()}  ${hour_wind_way[0].text()}   size:${hour_wind_time.size} / ${hour_wind_num.size} / ${hour_wind_way.size}"
                        )
                        Log.d(
                            "lys",
                            "시간별 습도 : ${hour_hum_time[0].text()} ${hour_hum_num[0].text()}   size:${hour_hum_time.size} / ${hour_hum_num.size}"
                        )

//                        Log.d("lys","주간예보 : ${week[0]}")

                        Log.d(
                            "lys",
                            "주간예보 : ${week_weekend.text()} / ${week_date.text()} / ${week_am_rain.text()} / ${week_pm_rain.text()} / ${week_low_temp.text()} / ${week_high_temp.text()}"
                        )
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("lys", "error : ${e.message}")
            }
        }
    }
}