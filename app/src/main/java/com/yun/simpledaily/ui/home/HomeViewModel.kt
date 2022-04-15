package com.yun.simpledaily.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.data.Constant.COMPARE_WEATHER
import com.yun.simpledaily.data.Constant.DUST_UV
import com.yun.simpledaily.data.Constant.HOURLY
import com.yun.simpledaily.data.Constant.HOURLY_HUMIDITY_NUM
import com.yun.simpledaily.data.Constant.HOURLY_HUMIDITY_TIME
import com.yun.simpledaily.data.Constant.HOURLY_PRECIPITATION_NUM
import com.yun.simpledaily.data.Constant.HOURLY_PRECIPITATION_PERCENT
import com.yun.simpledaily.data.Constant.HOURLY_PRECIPITATION_TIME
import com.yun.simpledaily.data.Constant.HOURLY_WEATHER_INFO
import com.yun.simpledaily.data.Constant.HOURLY_WEATHER_NUM
import com.yun.simpledaily.data.Constant.HOURLY_WEATHER_TIME
import com.yun.simpledaily.data.Constant.HOURLY_WIND_DIRECTION
import com.yun.simpledaily.data.Constant.HOURLY_WIND_NUM
import com.yun.simpledaily.data.Constant.HOURLY_WIND_TIME
import com.yun.simpledaily.data.Constant.NOW_WEATHER
import com.yun.simpledaily.data.Constant.SUMMARY_LIST
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.Constant.TEMPERATURE
import com.yun.simpledaily.data.Constant.WEATHER
import com.yun.simpledaily.data.Constant.WEATHER_IMG
import com.yun.simpledaily.data.Constant.WEATHER_LOCATION_NM
import com.yun.simpledaily.data.Constant.WEEK_DOW
import com.yun.simpledaily.data.Constant.WEEK_FORECAST
import com.yun.simpledaily.data.Constant.WEEK_HIGHEST
import com.yun.simpledaily.data.Constant.WEEK_LOWEST
import com.yun.simpledaily.data.Constant.WEEK_PRECIPITATION
import com.yun.simpledaily.data.Constant.WEEK_PRECIPITATION_DETAIL
import com.yun.simpledaily.data.Constant.WEEK_TIME
import com.yun.simpledaily.data.model.RealTimeModel
import com.yun.simpledaily.data.repository.api.Api
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.jsoup.Jsoup

class HomeViewModel(
    application: Application,
    private val api: Api
) : BaseViewModel(application) {

    val imagePath = MutableLiveData("")
    val location = MutableLiveData("")
    val temperature = MutableLiveData("")
    val weather = MutableLiveData("")
    val compare = MutableLiveData("")
    val weatherDetail = MutableLiveData("")
    val dust = MutableLiveData("")
    val uDust = MutableLiveData("")
    val uv = MutableLiveData("")

    init {
        addWeather()
    }

    fun realtime() {
        // 시그널 실시간 검색어
        // api : https://api.signal.bz/news/realtime
        // 사이트 : https://www.signal.bz/

        viewModelScope.launch {
            try {
                (callApi(api.realtime()) as RealTimeModel.RS).run {
                    Log.d(TAG, "result : ${this.top10}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.printStackTrace()}")
            }
        }
    }

    fun addWeather() {

//        https://ssl.pstatic.net/sstatic/keypage/outside/scui/weather_new_new/img/weather_svg/icon_flat_wt41.svg
        // 1부터 41까지 날씨 이미지

        val url = mContext.getString(R.string.weather_url) + "김포시고촌읍날씨"

        viewModelScope.async {
            try {
                launch(newSingleThreadContext(WEATHER)) {



                    // 전체 데이터
                    val doc = Jsoup.connect(url).get()


                    // 검새어 (ex - 서울날씨, 부산날씨
                    location.postValue(doc.select(WEATHER_LOCATION_NM)[0].text())

                    // 날씨 이미지
                    val path = doc.select(WEATHER_IMG)[0].className().replace("wt_","").replace("ico_","flat_").replace(" ","_")
                    imagePath.postValue("${mContext.getString(R.string.weather_img_url)}$path.svg")
//
//                    // 현재 온도
                    temperature.postValue(doc.select(TEMPERATURE)[0].text().replace("온도","온도 : "))
//
//                    // 현재 날씨
                    weather.postValue("|  ${doc.select(NOW_WEATHER)[0].text()}")
//
//                    // 어제보다 높거나 낮은 정도
                    compare.postValue("어제보다 ${doc.select(COMPARE_WEATHER)[0].text()}")

                    Log.d(TAG,"어제보다 ${doc.select(COMPARE_WEATHER)[0].text()}")

                    // 현재 강수, 습도, 바람 (ex - 강수확률 0% 습도 25% 바람(북서풍) 2m/s
                    weatherDetail.postValue(doc.select(SUMMARY_LIST)[0].text().replace("%","% | "))

                    // 미세먼지, 초미세먼지
                    dust.postValue(doc.select(DUST_UV)[0].text())
                    uDust.postValue(doc.select(DUST_UV)[1].text())

                    // 자외선
                    uv.postValue(doc.select(DUST_UV)[2].text())

                    Log.d(TAG,"${doc.select(DUST_UV)[0].text()}  ${doc.select(DUST_UV)[1].text()}  ${doc.select(DUST_UV)[2].text()}")

                    // 시간별 정보 모든 데이터
                    val hour_data = doc.select(HOURLY)


                    // 시간별 날씨 디테일(날짜, 온도, 상태)
                    val hour_wea_time = hour_data[0].select(HOURLY_WEATHER_TIME)
                    val hour_wea_num = hour_data[0].select(HOURLY_WEATHER_NUM)
                    val hour_wea_wea = hour_data[0].select(HOURLY_WEATHER_INFO)

                    // 시간별 강수 디테일(닐짜, 강수량, 확률)
                    val hour_rain_time = hour_data[1].select(HOURLY_PRECIPITATION_TIME)
                    val hour_rain_num = hour_data[1].select(HOURLY_PRECIPITATION_NUM)
                    val hour_rain_per = hour_data[1].select(HOURLY_PRECIPITATION_PERCENT)

                    // 시간별 바람 디테일(날짜, 풍속, 방향)
                    val hour_wind_time = hour_data[2].select(HOURLY_WIND_TIME)
                    val hour_wind_num = hour_data[2].select(HOURLY_WIND_NUM)
                    val hour_wind_way = hour_data[2].select(HOURLY_WIND_DIRECTION)

                    // 시간별 습도 디테일(날짜, 습도)
                    val hour_hum_time = hour_data[3].select(HOURLY_HUMIDITY_TIME)
                    val hour_hum_num = hour_data[3].select(HOURLY_HUMIDITY_NUM)

                    // 주간 예보
                    val week = doc.select(WEEK_FORECAST)



                    // 주간 예보 디테일 - 요일
                    val week_weekend = week[0].select(WEEK_DOW)
                    // 주간 예보 디테일 - 날짜
                    val week_date = week[0].select(WEEK_TIME)
                    // 주간 예보 디테일 - 오전 강수 확률
                    val week_am_rain =
                        week[0].select(WEEK_PRECIPITATION)[0].select(WEEK_PRECIPITATION_DETAIL)
                    // 주간 예보 디테일 - 오후 강수 확률
                    val week_pm_rain =
                        week[0].select(WEEK_PRECIPITATION)[1].select(WEEK_PRECIPITATION_DETAIL)
                    // 주간 예보 디테일 - 최저기온
                    val week_low_temp = week[0].select(WEEK_LOWEST)
                    // 주간 예보 디테일 - 최고기온
                    val week_high_temp = week[0].select(WEEK_HIGHEST)




//                        Log.d(TAG, "${location.value} / ${temperature.value} / ${compare.value} / ${weather.value} / $list / $dus1 / $dus2 / $sun")

                        Log.d(
                            TAG,
                            "시간별 날씨 : ${hour_wea_time[0].text()} ${hour_wea_num[0].text()}  ${hour_wea_wea[0].text()}   size:${hour_wea_time.size} / ${hour_wea_num.size} / ${hour_wea_wea.size}"
                        )
                        Log.d(
                            TAG,
                            "시간반 강수 : ${hour_rain_time[0].text()} ${hour_rain_num[0].text()}  ${hour_rain_per[0].text()}   size:${hour_rain_time.size} / ${hour_rain_num.size} / ${hour_rain_per.size}"
                        )
                        Log.d(
                            TAG,
                            "시간별 바람 : ${hour_wind_time[0].text()} ${hour_wind_num[0].text()}  ${hour_wind_way[0].text()}   size:${hour_wind_time.size} / ${hour_wind_num.size} / ${hour_wind_way.size}"
                        )
                        Log.d(
                            TAG,
                            "시간별 습도 : ${hour_hum_time[0].text()} ${hour_hum_num[0].text()}   size:${hour_hum_time.size} / ${hour_hum_num.size}"
                        )

//                        Log.d("lys","주간예보 : ${week[0]}")

                        Log.d(
                            TAG,
                            "주간예보 : ${week_weekend.text()} / ${week_date.text()} / ${week_am_rain.text()} / ${week_pm_rain.text()} / ${week_low_temp.text()} / ${week_high_temp.text()}"
                        )

                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("lys", "error : ${e.message}")
            }
        }
    }
}