package com.yun.simpledaily.ui.hourly

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant.HUM
import com.yun.simpledaily.data.Constant.RAIN
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.Constant.WIND
import com.yun.simpledaily.data.model.HourlyWeatherModel
import com.yun.simpledaily.databinding.FragmentHourlyWeatherBinding
import com.yun.simpledaily.databinding.ItemHourlyRainBinding
import com.yun.simpledaily.databinding.ItemHourlyWeatherBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HourlyWeatherFragment
    :
    BaseBindingFragment<FragmentHourlyWeatherBinding, HourlyWeatherViewModel>(HourlyWeatherViewModel::class.java) {
    override val viewModel: HourlyWeatherViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_hourly_weather
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.hourlyWeather

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.hideBottomView()

        arguments.run {
            this?.getParcelableArrayList<HourlyWeatherModel.Weather>("rain")?.run {
                viewModel.hourlyRainList.value = this
            }

            this?.getParcelableArrayList<HourlyWeatherModel.Weather>("wind")?.run {
                viewModel.hourlyWindList.value = this
            }

            this?.getParcelableArrayList<HourlyWeatherModel.Weather>("hum")?.run {
                viewModel.hourlyHumList.value = this
            }
        }

        viewModel.apply {

        }

        binding.apply {
            rvHourlyRain.run {
                adapter = object :
                    BaseRecyclerAdapter.Create<HourlyWeatherModel.Weather, ItemHourlyRainBinding>(
                        R.layout.item_hourly_rain,
                        bindingVariableId = BR.itemHourlyRain,
                        bindingListener = BR.hourlyRainItemListener
                    ) {
                    override fun onItemClick(item: HourlyWeatherModel.Weather, view: View) {

                    }
                }
            }

            rvHourlyWind.run {
                adapter = object :
                BaseRecyclerAdapter.Create<HourlyWeatherModel.Weather, ItemHourlyRainBinding>(
                    R.layout.item_hourly_rain,
                    bindingVariableId = BR.itemHourlyRain,
                    bindingListener = BR.hourlyRainItemListener
                ){
                    override fun onItemClick(item: HourlyWeatherModel.Weather, view: View) {

                    }
                }
            }

            rvHourlyHum.run {
                adapter = object :
                BaseRecyclerAdapter.Create<HourlyWeatherModel.Weather, ItemHourlyRainBinding>(
                    R.layout.item_hourly_rain,
                    bindingVariableId = BR.itemHourlyRain,
                    bindingListener = BR.hourlyRainItemListener
                ){
                    override fun onItemClick(item: HourlyWeatherModel.Weather, view: View) {

                    }
                }
            }

        }
    }
}