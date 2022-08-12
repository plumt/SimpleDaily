package com.yun.simpledaily.ui.schedule.viewpager.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.custom.CalendarUtils.Companion.getFormatString
import com.yun.simpledaily.data.model.CalendarModels
import com.yun.simpledaily.databinding.FragmentScheduleListBinding
import com.yun.simpledaily.databinding.ItemCalendarBinding
import com.yun.simpledaily.ui.schedule.ScheduleViewModel
import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel

class ScheduleListFragment
    :
    BaseBindingFragment<FragmentScheduleListBinding, ScheduleListViewModel>(ScheduleListViewModel::class.java) {
    override val viewModel: ScheduleListViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_schedule_list
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.scheduleList

    private val viewPagerFragment: ScheduleViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerFragment.selectDate.observe(viewLifecycleOwner) {
            if (it != null && it != "") {
                viewModel.selectDate.value = it
                var date = it
                date = date.replace("년 ", "-").replace("월", "-") + "01"
                viewModel.selectMonthEvent(
                    DateTime(date).getFormatString("yyyyMM01").toLong(),
                    DateTime(date).getFormatString("yyyyMM31").toLong()
                )
            }
        }

        binding.rvSchedule.run {
            adapter = object : BaseRecyclerAdapter.Create<CalendarModels, ItemCalendarBinding>(
                R.layout.item_calendar,
                bindingVariableId = BR.itemCalendar,
                bindingListener = BR.calendarItemListener
            ) {
                override fun onItemLongClick(item: CalendarModels, view: View): Boolean {
                    return true
                }
                override fun onItemClick(item: CalendarModels, view: View) {
                    Toast.makeText(requireContext(), item.event, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}