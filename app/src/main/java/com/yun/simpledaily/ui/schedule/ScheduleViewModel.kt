package com.yun.simpledaily.ui.schedule

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yun.simpledaily.base.BaseViewModel

class ScheduleViewModel(
    application: Application
) : BaseViewModel(application) {
    val addScheduleEvent = MutableLiveData(false)
    val screenPosition = MutableLiveData(0)
    val selectDate = MutableLiveData("")
}