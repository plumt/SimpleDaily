package com.yun.simpledaily.ui.schedule.viewpager.calendar

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yun.simpledaily.base.BaseViewModel
import org.joda.time.DateTime

class CalendarViewModel(
    application: Application
) : BaseViewModel(application) {

    val nowDate: Long = DateTime().withTimeAtStartOfDay().millis

    val selectDate = MutableLiveData("")

}