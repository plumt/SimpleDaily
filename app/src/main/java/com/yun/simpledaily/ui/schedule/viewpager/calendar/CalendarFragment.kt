package com.yun.simpledaily.ui.schedule.viewpager.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.custom.CalendarUtils
import com.yun.simpledaily.custom.CalendarUtils.Companion.getFormatString
import com.yun.simpledaily.custom.CalendarView
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.databinding.FragmentCalendarBinding
import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel

class CalendarFragment
    : BaseBindingFragment<FragmentCalendarBinding, CalendarViewModel>(CalendarViewModel::class.java){
    override val viewModel: CalendarViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_calendar
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cvCalendar.listener = object : CalendarView.OnCalendarListener {
                override fun onClick(dateTime: DateTime) {
                    Log.d(TAG,"$dateTime")
                    viewModel.selectDate.value = dateTime.getFormatString("yyyy-MM-dd")
                    setCalendar()
                }
            }
        }
        setCalendar()
    }

    private fun addSchedule(){

    }

    private fun setCalendar(value: Int = 0){
        binding.cvCalendar.let {
            val dateTime = arrayListOf<DateTime>()
//            dateTime.add(DateTime.parse("2022-04-22"))
//            dateTime.add(DateTime.parse("2022-04-26"))
            viewModel.run {
                if(selectDate.value == ""){
                    it.initCalendar(
                        DateTime(nowDate),
                        CalendarUtils.getMonthList(DateTime(nowDate)),
                        DateTime.now(),
                        eventDate = dateTime
                    )
                } else{
                    it.current!!.plusMonths(value).run {
                        it.initCalendar(
                            this,
                            CalendarUtils.getMonthList(this),
                            DateTime.parse(selectDate.value),
                            eventDate = dateTime
                        )
                    }
                }
            }
        }
    }
}