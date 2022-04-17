package com.yun.simpledaily.ui.calendar

import android.os.Bundle
import android.view.View
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.databinding.FragmentCalendarBinding
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

    }
}