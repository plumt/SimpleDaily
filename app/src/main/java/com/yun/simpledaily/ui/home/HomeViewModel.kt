package com.yun.simpledaily.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.custom.CalendarUtils.Companion.getFormatString
import com.yun.simpledaily.data.Constant.COMPARE_WEATHER
import com.yun.simpledaily.data.Constant.DUST
import com.yun.simpledaily.data.Constant.DUST_UV
import com.yun.simpledaily.data.Constant.EXCHAGE_LIST
import com.yun.simpledaily.data.Constant.EXCHANGE
import com.yun.simpledaily.data.Constant.HOURLY
import com.yun.simpledaily.data.Constant.HOURLY_HUMIDITY_NUM
import com.yun.simpledaily.data.Constant.HOURLY_HUMIDITY_TIME
import com.yun.simpledaily.data.Constant.HOURLY_PRECIPITATION_NUM
import com.yun.simpledaily.data.Constant.HOURLY_PRECIPITATION_PERCENT
import com.yun.simpledaily.data.Constant.HOURLY_PRECIPITATION_TIME
import com.yun.simpledaily.data.Constant.HOURLY_WEATHER_IMG
import com.yun.simpledaily.data.Constant.HOURLY_WEATHER_INFO
import com.yun.simpledaily.data.Constant.HOURLY_WEATHER_NUM
import com.yun.simpledaily.data.Constant.HOURLY_WEATHER_TIME
import com.yun.simpledaily.data.Constant.HOURLY_WIND_DIRECTION
import com.yun.simpledaily.data.Constant.HOURLY_WIND_NUM
import com.yun.simpledaily.data.Constant.HOURLY_WIND_TIME
import com.yun.simpledaily.data.Constant.JAPAN
import com.yun.simpledaily.data.Constant.MEMO
import com.yun.simpledaily.data.Constant.NEWS
import com.yun.simpledaily.data.Constant.NOW_WEATHER
import com.yun.simpledaily.data.Constant.REAL_TIME
import com.yun.simpledaily.data.Constant.SCHEDULE
import com.yun.simpledaily.data.Constant.SEARCH_WEATHER
import com.yun.simpledaily.data.Constant.SUMMARY_LIST
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.Constant.TEMPERATURE
import com.yun.simpledaily.data.Constant.UDUST
import com.yun.simpledaily.data.Constant.UV
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
import com.yun.simpledaily.data.Constant.WEEK_WEATHER_IMG
import com.yun.simpledaily.data.Constant._HOURLY
import com.yun.simpledaily.data.model.*
import com.yun.simpledaily.data.repository.DB
import com.yun.simpledaily.data.repository.api.Api
import com.yun.simpledaily.util.PreferenceManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.joda.time.DateTime
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.regex.Pattern

class HomeViewModel(
    application: Application,
    private val api: Api,
    private val db: DB,
    private val sharedPreferences: PreferenceManager
) : BaseViewModel(application) {

    val hourlyWeatherList = ListLiveData<HourlyWeatherModel.Weather>()
    val hourlyRainList = ListLiveData<HourlyWeatherModel.Weather>()
    val hourlyWindList = ListLiveData<HourlyWeatherModel.Weather>()
    val hourlyHumList = ListLiveData<HourlyWeatherModel.Weather>()
    val weekWeatherList = ListLiveData<WeekWeatherModel.RS>()
    val realTimeTop10 = ListLiveData<RealTimeModel.Top10>()
    val popularNews = ListLiveData<RealTimeModel.Articles>()
    val naverNews = ListLiveData<RealTimeModel.Naver>()
    val memoList = ListLiveData<MemoModels>()
    val scheduleEventList = ListLiveData<CalendarModels>()
    val exchangeList = ListLiveData<ExchangeModel>()

    val nowWeather = MutableLiveData<NowWeatherModel.Weather>()

    val dust = MutableLiveData("")
    val uDust = MutableLiveData("")
    val uv = MutableLiveData("")

    val isMoveNav = MutableLiveData("")

    val isShowWeather = MutableLiveData<Boolean>()
    val isShowHourly = MutableLiveData<Boolean>()
    val isShowRealTime = MutableLiveData<Boolean>()
    val isShowNews = MutableLiveData<Boolean>()
    val isShowMemo = MutableLiveData<Boolean>()
    val isShowSchedule = MutableLiveData<Boolean>()
    val isShowExchange = MutableLiveData<Boolean>()


    val searchLocation = MutableLiveData("")

    val loading = MutableLiveData(false)

    val memoSize = MutableLiveData(0)
    val scheduleSize = MutableLiveData(0)

    val isError = MutableLiveData(false)

    init {
        preferencesCheck()
    }

    private fun preferencesCheck() {
        sharedPreferences.run {
            if (getString(mContext, WEATHER) == "") {
                setString(mContext, WEATHER, "true")
                setString(mContext, _HOURLY, "true")
                setString(mContext, REAL_TIME, "true")
                setString(mContext, NEWS, "true")
                setString(mContext, MEMO, "true")
                setString(mContext, SCHEDULE, "true")
                setString(mContext, EXCHANGE, "true")
            }
            isShowWeather.value = getString(mContext, WEATHER).toBoolean()
            isShowHourly.value = getString(mContext, _HOURLY).toBoolean()
            isShowRealTime.value = getString(mContext, REAL_TIME).toBoolean()
            isShowNews.value = getString(mContext, NEWS).toBoolean()
            isShowMemo.value = getString(mContext, MEMO).toBoolean()
            isShowSchedule.value = getString(mContext, SCHEDULE).toBoolean()
            isShowExchange.value = getString(mContext, EXCHANGE).toBoolean()
        }
    }

    fun callApiList() {
        schedule()
//        exchange()
        //TODO 날씨 - 완료
        //TODO 뉴스 - 완료
        //TODO 인터넷 원하는 링크 바로가기 기능도 추가 - 예정
        //TODO 캘린더로 일정 관리 기능 추가 - 완료
        //TODO 메모 기능 추가 - 완료
        //TODO 코로나 확진자수 필요한지 - 필요없음
        //TODO 원하는 종목 주가 https://finance.daum.net/domestic/search?q=메지온 - 예정
        //TODO 환율 정보 - 완료
    }

    private fun schedule() {
        if (checkedShowBoard(SCHEDULE)) {
            memo()
            return
        }
        viewModelScope.async {
            try {
                val nowDate =
                    DateTime(DateTime().withTimeAtStartOfDay().millis).getFormatString("yyyyMMdd")
                        .toLong()
                val list = arrayListOf<CalendarModels>()
                launch(newSingleThreadContext(SCHEDULE)) {
                    val data = db.calendarDao().selectEvent(nowDate)
                    data.forEachIndexed { index, calendarModel ->
                        list.add(
                            CalendarModels(
                                index,
                                0,
                                dateConvert(calendarModel.date.toString()),
                                calendarModel.event
                            )
                        )
                    }
                }.join()
                if(list.size > 0) {
                    list[list.lastIndex].last = true
                    scheduleEventList.value = list
                }
                scheduleSize.value = scheduleEventList.value!!.size
                memo()
            } catch (e: java.lang.Exception) {
                error()
                e.printStackTrace()
            }
        }
    }

    private fun memo() {
        if (checkedShowBoard(MEMO)) {
            realtime()
            return
        }
        viewModelScope.async {
            try {
                val list = arrayListOf<MemoModels>()
                launch(newSingleThreadContext(MEMO)) {
                    val data = db.memoDao().selectMemo3()
                    val size = if (data.isEmpty()) 0 else data.size - 1
                    data.forEachIndexed { index, memoModel ->
                        list.add(
                            MemoModels(
                                index,
                                0,
                                memoModel.id,
                                memoModel.title,
                                memoModel.memo,
                                true,
                                size
                            )
                        )
                    }
                }.join()
                memoList.value = list
                memoSize.value = memoList.sizes()
                realtime()
            } catch (e: java.lang.Exception) {
                error()
                e.printStackTrace()
            }
        }
    }

    private fun realtime() {
        if (checkedShowBoard(REAL_TIME) && checkedShowBoard(NEWS)) {
            weather()
            return
        }
        loading.value = true
        // api : https://api.signal.bz/news/realtime
        // 사이트 : https://www.signal.bz/

        viewModelScope.launch {
            try {
                (callApi(api.realtime()) as RealTimeModel.RS).run {
                    realTimeTop10.value = this.top10
                    setId(realTimeTop10.value!!)
                    popularNews.value = this.articles
                    setId(popularNews.value!!)
                    naverNews.value = this.naver
                    setId(naverNews.value!!)
                    weather()
                }
            } catch (e: Exception) {
                error()
                e.printStackTrace()
            }
        }
    }

    // 환율
    private fun exchange(){
        if (checkedShowBoard(EXCHANGE)) {
            loading.value = false
            return
        }
        viewModelScope.async {
            try {
                val arrayList = arrayListOf<ExchangeModel>()
                arrayList.add(ExchangeModel(0,0,"통화명","","매매기준율","전일대비","등락률"))
                var doc: Document? = null
                launch(newSingleThreadContext(EXCHANGE)){
                    doc = Jsoup.connect(mContext.getString(R.string.exchange_url)).get()
                }.join()

                val regExp = Regex("[^0-9|.]")
                doc!!.body().select(EXCHAGE_LIST).forEachIndexed { index, element ->
                    val list = arrayListOf<String>()
                    element.text().split(" ").forEach { data ->
                        list.add(data)
                    }
                    if (list[0] == JAPAN) {
                        list[4] = list[4].replace(regExp, "")
                        list[1] = list[1] + list[2]
                        list.removeAt(2)
                    } else list[3] = list[3].replace(regExp, "")

                    arrayList.add(
                        ExchangeModel(
                            index + 1,
                            0,
                            list[0],
                            list[1],
                            list[2],
                            list[3],
                            list[4]
                        )
                    )
                }
                exchangeList.value = arrayList
                Log.d("lys","exchangeList.value : ${exchangeList.value}")
                loading.value = false
            } catch (e: java.lang.Exception){
                error()
                Log.e("lys","error : ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun weather() {
        if ((checkedShowBoard(WEATHER) && checkedShowBoard(_HOURLY)) || searchLocation.value == "") {
            exchange()
            return
        }

        loading.value = true
//        https://ssl.pstatic.net/sstatic/keypage/outside/scui/weather_new_new/img/weather_svg/icon_flat_wt41.svg

        viewModelScope.async {
            try {
                var doc: Document? = null
                launch(newSingleThreadContext(WEATHER)) {
                    // 전체 데이터
                    doc =
                        Jsoup.connect(mContext.getString(R.string.weather_url) + searchLocation.value + SEARCH_WEATHER)
                            .get()
                }.join()

                addNowWeather(doc!!)

                // 시간별 날씨 디테일(날짜, 온도, 상태, 이미지)
                hourlyWeatherList.value = addHourlyWeather(
                    doc!!.select(HOURLY)[0].select(HOURLY_WEATHER_TIME),
                    doc!!.select(HOURLY)[0].select(HOURLY_WEATHER_NUM),
                    doc!!.select(HOURLY)[0].select(HOURLY_WEATHER_INFO),
                    doc!!.select(HOURLY)[0].select(HOURLY_WEATHER_IMG)
                )

//                 시간별 강수 디테일(닐짜, 강수량, 확률)
                hourlyRainList.value = addHourlyWeather(
                    doc!!.select(HOURLY)[1].select(HOURLY_PRECIPITATION_TIME),
                    doc!!.select(HOURLY)[1].select(HOURLY_PRECIPITATION_NUM),
                    doc!!.select(HOURLY)[1].select(HOURLY_PRECIPITATION_PERCENT),
                    null,
                    "mm"
                )

                // 시간별 바람 디테일(날짜, 풍속, 방향)
                hourlyWindList.value = addHourlyWeather(
                    doc!!.select(HOURLY)[2].select(HOURLY_WIND_TIME),
                    doc!!.select(HOURLY)[2].select(HOURLY_WIND_NUM),
                    doc!!.select(HOURLY)[2].select(HOURLY_WIND_DIRECTION),
                    null,
                    "m/s"
                )

                // 시간별 습도 디테일(날짜, 습도)
                hourlyHumList.value = addHourlyWeather(
                    doc!!.select(HOURLY)[3].select(HOURLY_HUMIDITY_TIME),
                    doc!!.select(HOURLY)[3].select(HOURLY_HUMIDITY_NUM),
                    null, null, "%"
                )

                weekWeatherList.value = addWeekWeather(doc!!.select(WEEK_FORECAST))
                exchange()
            } catch (e: Exception) {
                error()
                e.printStackTrace()
            }
        }
    }

    // 주간 예보
    private fun addWeekWeather(week: Elements): ArrayList<WeekWeatherModel.RS> {
        val array = arrayListOf<WeekWeatherModel.RS>()
        week.forEachIndexed { index, element ->
            array.add(
                WeekWeatherModel.RS(
                    index,
                    0,
                    week[index].select(WEEK_DOW).text(),
                    week[index].select(WEEK_TIME).text()
                        .substring(0, week[index].select(WEEK_TIME).text().lastIndex)
                        .replace(".", "/"),
                    week[index].select(WEEK_PRECIPITATION)[0].select(WEEK_PRECIPITATION_DETAIL)
                        .text(),
                    week[index].select(WEEK_PRECIPITATION)[1].select(WEEK_PRECIPITATION_DETAIL)
                        .text(),
                    week[index].select(WEEK_LOWEST).text().replace("기온", ""),
                    week[index].select(WEEK_HIGHEST).text().replace("기온", ""),
                    setImagePath(week[index].select(WEEK_WEATHER_IMG)[0].className()),
                    setImagePath(week[index].select(WEEK_WEATHER_IMG)[1].className())
                )
            )
        }
        return array
    }

    // 현재 날씨
    private fun addNowWeather(doc: Document) {
        nowWeather.value = NowWeatherModel.Weather(
            // 검새어 (ex - 서울날씨, 부산날씨
            location = doc.select(WEATHER_LOCATION_NM)[0].text(),
            // 날씨 이미지
            imagePath = setImagePath(doc.select(WEATHER_IMG)[0].className()),
            // 현재 온도
            temperature = doc.select(TEMPERATURE)[0].text().replace("온도", "온도 : "),
            // 현재 날씨
            weather = "|  ${doc.select(NOW_WEATHER)[0].text()}",
            // 어제보다 높거나 낮은 정도
            compare = "어제보다 ${doc.select(COMPARE_WEATHER)[0].text()}",
            // 현재 강수, 습도, 바람 (ex - 강수확률 0% 습도 25% 바람(북서풍) 2m/s
            weatherDetail = doc.select(SUMMARY_LIST)[0].text().replace("%", "% | ")
        )

        // 미세먼지, 초미세먼지, 자외선, 일몰
        for (i in 0..3) {
            val str = doc.select(DUST_UV)[i].text()
            when {
                str.contains(UDUST) -> uDust.value = str
                str.contains(DUST) -> dust.value = str
                str.contains(UV) -> uv.value = str
            }
        }
    }

    // 시간별 날씨
    private fun addHourlyWeather(
        time: Elements,
        num: Elements,
        weather: Elements?,
        url: Elements?,
        subTitle: String = ""
    ): ArrayList<HourlyWeatherModel.Weather> {
        val hourly = arrayListOf<HourlyWeatherModel.Weather>()
        time.forEachIndexed { index, element ->
            hourly.add(
                HourlyWeatherModel.Weather(
                    index,
                    0,
                    element.text(),
                    if (num.size > index) num[index].text() else "",
                    if (weather == null || weather.size <= index || weather[index].text() == "-") "" else weather[index].text(),
                    if (url == null || url.size <= index) "" else setImagePath(url[index].className()),
                    subTitle
                )
            )
        }
        return hourly
    }

    private fun checkedShowBoard(type: String) =
        sharedPreferences.getString(mContext, type) == "false"

    private fun error() {
        if (!isError.value!!) {
            isError.value = true
            loading.value = false
        }
    }

    private fun dateConvert(date: String): String {
        return "${date.substring(0, 4)}-${date.substring(4, 6)}-${date.substring(6, 8)}"
    }

    // 이미지 url
    private fun setImagePath(url: String): String =
        "${mContext.getString(R.string.weather_img_url)}${
            url.replace("wt_", "").replace("ico_", "flat_").replace(" ", "_")
        }.svg"

    fun onClick(type: String) {
        isMoveNav.value = type
    }
}