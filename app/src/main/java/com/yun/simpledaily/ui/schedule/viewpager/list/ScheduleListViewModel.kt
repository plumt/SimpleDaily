package com.yun.simpledaily.ui.schedule.viewpager.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.model.CalendarModels
import com.yun.simpledaily.data.repository.DB
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class ScheduleListViewModel(
    application: Application,
    private val db: DB
) : BaseViewModel(application) {

    val eventMonthList = ListLiveData<CalendarModels>()
    private val isMonthEventSelectSuccess = MutableLiveData(false)
    val selectDate = MutableLiveData<String>("")

    fun selectMonthEvent(startDt: Long, endDt: Long) {
        viewModelScope.async {
            try {
                val list = arrayListOf<CalendarModels>()
                launch(newSingleThreadContext(Constant._SCHEDULE)) {
                    val data = db.calendarDao().selectMonth(startDt, endDt)
                    var title = ""
                    data.forEachIndexed { index, calendarModel ->
                        if (dateConvert(calendarModel.date.toString()) != title) {
                            title = dateConvert(calendarModel.date.toString())
                            val day =
                                if (index == 0) returnMonth(calendarModel.date.toString())
                                else returnDay(calendarModel.date.toString())
                            list.add(CalendarModels(list.size, 0, "", "", day))
                        }
                        list.add(
                            CalendarModels(
                                list.size,
                                0,
                                dateConvert(calendarModel.date.toString()),
                                calendarModel.event
                            )
                        )
                    }
                }.join()
                eventMonthList.value = list
                isMonthEventSelectSuccess.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun dateConvert(date: String): String {
        return "${date.substring(0, 4)}-${date.substring(4, 6)}-${date.substring(6, 8)}"
    }

    private fun returnMonth(date: String): String =
        "${date.substring(4, 6)}월 ${date.substring(6, 8)}일"

    private fun returnDay(date: String): String = "${date.substring(6, 8)}일"
}