package com.yun.simpledaily.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant.MEMO
import com.yun.simpledaily.data.Constant.NAVER_SEARCH_URL
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.Constant.WEATHER
import com.yun.simpledaily.data.model.HourlyWeatherModel
import com.yun.simpledaily.data.model.MemoModel
import com.yun.simpledaily.data.model.MemoModels
import com.yun.simpledaily.data.model.RealTimeModel
import com.yun.simpledaily.databinding.*
import com.yun.simpledaily.ui.main.MainActivity
import com.yun.simpledaily.ui.main.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment
    : BaseBindingFragment<FragmentHomeBinding, HomeViewModel>(HomeViewModel::class.java) {
    override val viewModel: HomeViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_home
    override fun onBackEvent() {}
    override fun initData(): Boolean = true
    override fun setVariable(): Int = BR.home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.searchLocation.observe(viewLifecycleOwner) {
            viewModel.searchLocation.value = it
        }

        viewModel.apply {

            isMoveNav.observe(viewLifecycleOwner) {
                when (it) {
                    MEMO -> (activity as MainActivity).binding.bottomNavView.selectedItemId = R.id.memo
                    WEATHER ->(activity as MainActivity).binding.bottomNavView.selectedItemId = R.id.setting
                }
//                if(it != "")isMoveNav.value = ""
            }

            searchLocation.observe(viewLifecycleOwner) {
                if (it != "") addWeather()
                else successCnt.value = successCnt.value!! + 1
            }

            loading.observe(viewLifecycleOwner) {
                sharedViewModel.isLoading.value = it
            }

            successCnt.observe(viewLifecycleOwner) {
                if (it == apiCnt) {
                    loading.value = false
                    successCnt.value = 0
                }
            }
        }

        binding.apply {

            rvMemo.run {
                adapter = object : BaseRecyclerAdapter.Create<MemoModels, ItemMemoBinding>(
                    R.layout.item_memo,
                    bindingVariableId = BR.itemMemo,
                    bindingListener = BR.memoListener
                ) {
                    override fun onItemClick(item: MemoModels, view: View) {
                        sharedViewModel.selectMemoId.value = item.id_
                        (activity as MainActivity).binding.bottomNavView.selectedItemId = R.id.memo
                    }
                }
            }

            rvHourlyWeather.run {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = object :
                    BaseRecyclerAdapter.Create<HourlyWeatherModel.RS, ItemHourlyWeatherBinding>(
                        R.layout.item_hourly_weather,
                        bindingVariableId = BR.itemHourlyWeather,
                        bindingListener = BR.hourlyWeatherItemListener
                    ) {
                    override fun onItemClick(item: HourlyWeatherModel.RS, view: View) {

                    }
                }
            }

            rvRealtimeTop10.run {
                adapter = object :
                    BaseRecyclerAdapter.Create<RealTimeModel.Top10, ItemRealtimeTop10Binding>(
                        R.layout.item_realtime_top10,
                        bindingVariableId = BR.itemRealTimeTop10,
                        bindingListener = BR.realTimeTop10ItemListener
                    ) {
                    override fun onItemClick(item: RealTimeModel.Top10, view: View) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(NAVER_SEARCH_URL + item.keyword)))
                    }
                }
            }

            rvPopularNews.run {
                adapter = object :
                    BaseRecyclerAdapter.Create<RealTimeModel.Articles, ItemPopularNewsBinding>(
                        R.layout.item_popular_news,
                        bindingVariableId = BR.itemPopularNews,
                        bindingListener = BR.popularNewsListener
                    ) {
                    override fun onItemClick(item: RealTimeModel.Articles, view: View) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
                    }
                }
            }
        }
    }
}