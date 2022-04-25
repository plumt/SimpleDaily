package com.yun.simpledaily.ui.schedule.viewpager.list

import android.os.Bundle
import android.view.View
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.databinding.FragmentScheduleListBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ScheduleListFragment
    : BaseBindingFragment<FragmentScheduleListBinding, ScheduleListViewModel>(ScheduleListViewModel::class.java){
    override val viewModel: ScheduleListViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_schedule_list
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.scheduleList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}