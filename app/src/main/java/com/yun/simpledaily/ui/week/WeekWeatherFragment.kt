package com.yun.simpledaily.ui.week

import android.os.Bundle
import android.view.View
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.model.WeekWeatherModel
import com.yun.simpledaily.databinding.FragmentWeekWeatherBinding
import com.yun.simpledaily.databinding.ItemWeekWeatherBinding
import org.koin.android.viewmodel.ext.android.viewModel

class WeekWeatherFragment
    : BaseBindingFragment<FragmentWeekWeatherBinding, WeekWeatherViewModel>(WeekWeatherViewModel::class.java){
    override val viewModel: WeekWeatherViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_week_weather
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.weekWeather

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.hideBottomView()

        arguments.run {
            this?.getParcelableArrayList<WeekWeatherModel.RS>("week")?.run {
                viewModel.weekWeatherList.value = this
            }
        }

        binding.apply {
            rvWeekWeather.run {
                adapter = object : BaseRecyclerAdapter.Create<WeekWeatherModel.RS, ItemWeekWeatherBinding>(
                    R.layout.item_week_weather,
                    bindingVariableId = BR.itemWeekWeather,
                    bindingListener = BR.weekWeatherListener
                ){
                    override fun onItemClick(item: WeekWeatherModel.RS, view: View) {

                    }
                }
            }
        }
        
    }
}