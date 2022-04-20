package com.yun.simpledaily.ui.hourly.viewpager.hum

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.model.HourlyWeatherModel
import com.yun.simpledaily.databinding.FragmentHourlyHumBinding
import com.yun.simpledaily.databinding.ItemHourlyHumBinding
import com.yun.simpledaily.databinding.ItemHourlyRainBinding
import com.yun.simpledaily.ui.hourly.HourlyWeatherViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HourlyHumFragment
    : BaseBindingFragment<FragmentHourlyHumBinding, HourlyHumViewModel>(HourlyHumViewModel::class.java){
    override val viewModel: HourlyHumViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_hourly_hum
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.hourlyHum

    val viewPagerFragment: HourlyWeatherViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerFragment.hourlyHumList.observe(viewLifecycleOwner){
            viewModel.hourlyHumList.value = it
        }

        binding.apply {

            rvHourlyWeather.run {
                adapter = object : BaseRecyclerAdapter.Create<HourlyWeatherModel.Weather, ItemHourlyHumBinding>(
                    R.layout.item_hourly_hum,
                    bindingVariableId = BR.itemHourlyHum,
                    bindingListener = BR.hourlyHumListener
                ){
                    override fun onItemClick(item: HourlyWeatherModel.Weather, view: View) {

                    }
                }
            }
        }
    }
}