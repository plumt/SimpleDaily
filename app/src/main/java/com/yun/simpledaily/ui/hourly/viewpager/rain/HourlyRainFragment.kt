package com.yun.simpledaily.ui.hourly.viewpager.rain

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.model.HourlyWeatherModel
import com.yun.simpledaily.databinding.FragmentHourlyRainBinding
import com.yun.simpledaily.databinding.ItemHourlyRainBinding
import com.yun.simpledaily.ui.hourly.HourlyWeatherViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HourlyRainFragment
    : BaseBindingFragment<FragmentHourlyRainBinding, HourlyRainViewModel>(HourlyRainViewModel::class.java){
    override val viewModel: HourlyRainViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_hourly_rain
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.hourlyRain

    val viewPagerFragment: HourlyWeatherViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerFragment.hourlyRainList.observe(viewLifecycleOwner){
            viewModel.hourlyRainList.value = it
        }

        binding.apply {

            rvHourlyWeather.run {
                adapter = object : BaseRecyclerAdapter.Create<HourlyWeatherModel.Weather, ItemHourlyRainBinding>(
                    R.layout.item_hourly_rain,
                    bindingVariableId = BR.itemHourlyRain,
                    bindingListener = BR.hourlyRainListener
                ){
                    override fun onItemClick(item: HourlyWeatherModel.Weather, view: View) {

                    }
                }
            }
        }
    }
}