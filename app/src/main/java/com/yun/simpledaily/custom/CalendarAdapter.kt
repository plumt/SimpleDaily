package com.yun.simpledaily.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.yun.simpledaily.R
import com.yun.simpledaily.custom.CalendarUtils.Companion.getMonthList
import org.joda.time.DateTime

class CalendarAdapter(@LayoutRes private val layoutRes: Int) : RecyclerView.Adapter<CalendarAdapter.Holder>() {
    private var start: Long = DateTime().withDayOfMonth(1).withTimeAtStartOfDay().millis

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.Holder {
        val root = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return Holder(root)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun getItemId(position: Int): Long {
        var temp = DateTime(start).plusMonths(position - START_POSITION).millis
        return temp
    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }

    override fun onBindViewHolder(holder: CalendarAdapter.Holder, position: Int) {
        holder.setTime(getItemId(position))
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var calendarView: CalendarView = itemView.findViewById(R.id.calendar)
        fun setTime(time:Long){
            calendarView.initCalendar(DateTime(time), getMonthList(DateTime(time)))
            calendarView.listener = object : CalendarView.OnCalendarListener{
                override fun onClick(dateTime: DateTime) {

                }
            }
        }
    }
}