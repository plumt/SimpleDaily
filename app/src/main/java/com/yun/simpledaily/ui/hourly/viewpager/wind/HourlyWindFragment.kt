package com.yun.simpledaily.ui.hourly.viewpager.wind

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.model.HourlyWeatherModel
import com.yun.simpledaily.databinding.FragmentHourlyWindBinding
import com.yun.simpledaily.databinding.ItemHourlyRainBinding
import com.yun.simpledaily.databinding.ItemHourlyWindBinding
import com.yun.simpledaily.ui.hourly.HourlyWeatherViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HourlyWindFragment
    : BaseBindingFragment<FragmentHourlyWindBinding, HourlyWindViewModel>(HourlyWindViewModel::class.java){
    override val viewModel: HourlyWindViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_hourly_wind
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.hourlyWind

    val viewPagerFragment: HourlyWeatherViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerFragment.hourlyWindList.observe(viewLifecycleOwner){
            viewModel.hourlyWindList.value = it
        }

        binding.apply {

            rvHourlyWeather.run {
                adapter = object : BaseRecyclerAdapter.Create<HourlyWeatherModel.Weather, ItemHourlyWindBinding>(
                    R.layout.item_hourly_wind,
                    bindingVariableId = BR.itemHourlyWind,
                    bindingListener = BR.hourlyWindListener
                ){
                    override fun onItemClick(item: HourlyWeatherModel.Weather, view: View) {

                    }
                }
            }
        }

    }
}