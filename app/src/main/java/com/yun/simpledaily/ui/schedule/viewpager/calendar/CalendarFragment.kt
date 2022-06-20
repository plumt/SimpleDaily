package com.yun.simpledaily.ui.schedule.viewpager.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.custom.CalendarUtils
import com.yun.simpledaily.custom.CalendarUtils.Companion.getFormatString
import com.yun.simpledaily.custom.CalendarView
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.model.CalendarModels
import com.yun.simpledaily.databinding.FragmentCalendarBinding
import com.yun.simpledaily.databinding.ItemCalendarBinding
import com.yun.simpledaily.ui.popup.EditTextPopup
import com.yun.simpledaily.ui.popup.OneButtonPopup
import com.yun.simpledaily.ui.schedule.ScheduleViewModel
import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel

class CalendarFragment
    :
    BaseBindingFragment<FragmentCalendarBinding, CalendarViewModel>(CalendarViewModel::class.java) {
    override val viewModel: CalendarViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_calendar
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.calendar

    private val viewPagerFragment: ScheduleViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            isMonthEventSelectSuccess.observe(viewLifecycleOwner) {
                if (it) {
                    isMonthEventSelectSuccess.value = false
                    selectEventCheck(selectDate.value == "")
                    setCalendar()
                }
            }

            selectDate.observe(viewLifecycleOwner) {
                selectEventCheck(it == "")
            }

            calTitle.observe(viewLifecycleOwner) {
                viewPagerFragment.selectDate.value = it
            }
        }

        viewPagerFragment.run {
            addScheduleEvent.observe(viewLifecycleOwner) {
                if (it) {
                    addScheduleEvent.value = false
                    addSchedule()
                }
            }
        }

        binding.run {

            btnNext.setOnClickListener {
                setCalendar(1)
            }

            btnPre.setOnClickListener {
                setCalendar(-1)
            }

            cvCalendar.listener = object : CalendarView.OnCalendarListener {
                override fun onClick(dateTime: DateTime) {
                    Log.d(TAG, "$dateTime")
                    viewModel.selectDate.value = dateTime.getFormatString("yyyy-MM-dd")
                    viewModel.calTitle.value = dateTime.getFormatString("yyyy년 MM월")
                    setCalendar()
                }
            }

            rvCalendar.run {
                adapter = object : BaseRecyclerAdapter.Create<CalendarModels, ItemCalendarBinding>(
                    R.layout.item_calendar,
                    bindingVariableId = BR.itemCalendar,
                    bindingListener = BR.calendarItemListener
                ) {
                    override fun onItemClick(item: CalendarModels, view: View) {
                        Toast.makeText(requireContext(), item.event, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun selectEventCheck(bool: Boolean) {
        viewModel.run {
            if (bool) selectEvent(DateTime(nowDate).getFormatString("yyyyMMdd").toLong())
            else selectEvent(DateTime.parse(selectDate.value).getFormatString("yyyyMMdd").toLong())
        }
    }

    private fun addSchedule() {
        Log.d(TAG, "addSchedule")
        EditTextPopup().apply {
            showPopup(
                requireContext(),
                "일정",
                "일정을 입력해 주세요"
            )
            setDialogListener(object : EditTextPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean, memo: String) {
                    if (result && memo != "") {
                        viewModel.insertEvent(memo)
                    }
                }
            })
        }
    }

    // TODO 커스텀 스피너 추가해서 삭제, 수정 기능 추가해야 함

    private fun showOnePopup() {
        OneButtonPopup().apply {
            showPopup(
                requireContext(),
                requireContext().getString(R.string.notice),
                requireContext().getString(R.string.toast_reset),
                true
            )
            setDialogListener(object : OneButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {}
            })
        }
    }

    private fun setCalendar(value: Int = 0) {
        binding.cvCalendar.let {
            val dateTime = arrayListOf<DateTime>()
            viewModel.eventMonthList.value!!.forEachIndexed { index, calendarModels ->
                if (index > 0 && viewModel.eventMonthList.value!![index - 1].date != viewModel.eventMonthList.value!![index].date || index == 0) {
                    dateTime.add(DateTime.parse(calendarModels.date))
                }
            }
            viewModel.run {
                if (selectDate.value == "") {
                    it.initCalendar(
                        DateTime(nowDate),
                        CalendarUtils.getMonthList(DateTime(nowDate)),
                        DateTime.now(),
                        eventDate = dateTime
                    )
                    selectDate.value = DateTime(nowDate).getFormatString("yyyy-MM-dd")
                    calTitle.value = DateTime(nowDate).getFormatString("yyyy년 MM월")
                } else {
                    it.current!!.plusMonths(value).run {
                        it.initCalendar(
                            this,
                            CalendarUtils.getMonthList(this),
                            DateTime.parse(selectDate.value),
                            eventDate = dateTime
                        )
                    }
                    calTitle.value = DateTime(it.current).getFormatString("yyyy년 MM월")
                }
            }
        }
    }
}