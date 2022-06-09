package com.yun.simpledaily.ui.schedule

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.databinding.FragmentScheduleBinding
import com.yun.simpledaily.ui.schedule.viewpager.calendar.CalendarFragment
import com.yun.simpledaily.ui.schedule.viewpager.list.ScheduleListFragment
import org.koin.android.viewmodel.ext.android.viewModel

class ScheduleFragment
    : BaseBindingFragment<FragmentScheduleBinding, ScheduleViewModel>(ScheduleViewModel::class.java){
    override val viewModel: ScheduleViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_schedule
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.schedule

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            binding.apply {

                imgAddSchedule.setOnClickListener {
                    viewModel.addScheduleEvent.value = true
                }

                vpSchedule.run {
                    isUserInputEnabled = true
                    adapter = object : FragmentStateAdapter(this@ScheduleFragment) {
                        override fun getItemCount(): Int = 2
                        override fun createFragment(position: Int): Fragment {
                            return when (position) {
                                0 -> CalendarFragment()
                                1 -> ScheduleListFragment()
                                else -> Fragment()
                            }
                        }
                    }
                    getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                }

                TabLayoutMediator(tablayout, vpSchedule) { tab, position ->
                    tab.text = when (position) {
                        0 -> Constant.CAL
                        1 -> Constant.LIST
                        else -> ""
                    }
                }.attach()
            }
        }
    }
}