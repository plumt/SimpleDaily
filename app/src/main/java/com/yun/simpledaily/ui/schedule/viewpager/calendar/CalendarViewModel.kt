package com.yun.simpledaily.ui.schedule.viewpager.calendar

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.custom.CalendarUtils.Companion.getFormatString
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.model.CalendarModel
import com.yun.simpledaily.data.model.CalendarModels
import com.yun.simpledaily.data.model.MemoModel
import com.yun.simpledaily.data.model.MemoModels
import com.yun.simpledaily.data.repository.DB
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.joda.time.DateTime

class CalendarViewModel(
    application: Application,
    private val db: DB
) : BaseViewModel(application) {

    val nowDate: Long = DateTime().withTimeAtStartOfDay().millis

    val calTitle = MutableLiveData<String>().apply { 
        value = DateTime(nowDate).getFormatString("yyyy년 MM월")
    }

    val selectDate = MutableLiveData("")

    val isSelectSuccess = MutableLiveData(false)
    
    val isMonthEventSelectSuccess = MutableLiveData(false)

    val eventList = ListLiveData<CalendarModels>()

    val eventMonthList = ListLiveData<CalendarModels>()

    init {
        selectMonthEvent(DateTime(nowDate).getFormatString("yyyyMM01").toLong(), DateTime(nowDate).getFormatString("yyyyMM31").toLong())
    }

    fun insertEvent(event: String) {
        viewModelScope.async {
            try {
                launch(newSingleThreadContext(Constant.SCHEDULE)) {
                    db.calendarDao().insertEvent(
                        CalendarModel(
                            date = DateTime(selectDate.value).getFormatString("yyyyMMdd").toLong(),
                            event = event
                        )
                    )
                }.join()
                selectMonthEvent(DateTime(selectDate.value).getFormatString("yyyyMM01").toLong(),DateTime(selectDate.value).getFormatString("yyyyMM31").toLong())
                Log.d(Constant.TAG,"insertEvent Success, $event ${selectDate.value}")
            } catch (e: Exception) {
                Log.e(Constant.TAG, "insertEvent error")
                e.printStackTrace()
            }
        }
    }

    fun selectMonthEvent(startDt: Long, endDt: Long){
        viewModelScope.async {
            try {
                val list = arrayListOf<CalendarModels>()
                launch(newSingleThreadContext(Constant.SCHEDULE)) {
                    val data = db.calendarDao().selectMonth(startDt,endDt)
                    data.forEachIndexed { index, calendarModel ->
                        list.add(CalendarModels(index,0,dateConvert(calendarModel.date.toString()), calendarModel.event))
                    }
                }.join()
                Log.d(Constant.TAG,"selectEvent Success : ${list}")
                eventMonthList.value = list
                isMonthEventSelectSuccess.value = true
            } catch (e: Exception){
                Log.e(Constant.TAG,"selectMonthEvent error")
                e.printStackTrace()
            }
        }
    }

    fun selectEvent(date: Long){
        viewModelScope.async {
            try {
                val list = arrayListOf<CalendarModels>()
                launch(newSingleThreadContext(Constant.SCHEDULE)) {
                    val data = db.calendarDao().selectEvent(date)
                    data.forEachIndexed { index, calendarModel ->
                        list.add(CalendarModels(index,0,dateConvert(calendarModel.date.toString()), calendarModel.event))
                    }
                }.join()
                Log.d(Constant.TAG,"selectEvent Success : ${list}")
                eventList.value = list
                isSelectSuccess.value = true
            } catch (e: Exception){
                Log.e(Constant.TAG,"selectEvent error")
                e.printStackTrace()
            }
        }
    }
    private fun dateConvert(date: String): String {
        return "${date.substring(0, 4)}-${date.substring(4, 6)}-${date.substring(6, 8)}"
    }
}