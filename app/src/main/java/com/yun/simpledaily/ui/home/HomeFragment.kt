package com.yun.simpledaily.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant.MEMO
import com.yun.simpledaily.data.Constant.NAVER_SEARCH_URL
import com.yun.simpledaily.data.Constant.NEWS
import com.yun.simpledaily.data.Constant.SCHEDULE
import com.yun.simpledaily.data.Constant.WEATHER
import com.yun.simpledaily.data.Constant.WEEK
import com.yun.simpledaily.data.Constant._HOURLY
import com.yun.simpledaily.data.model.*
import com.yun.simpledaily.databinding.*
import com.yun.simpledaily.ui.main.MainActivity
import com.yun.simpledaily.ui.popup.OneButtonPopup
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

        sharedViewModel.run {
            showBottomView()
            searchLocation.observe(viewLifecycleOwner) {
                viewModel.searchLocation.value = it
                viewModel.callApiList()
            }
        }

        viewModel.run {

            isError.observe(viewLifecycleOwner) {
                if (it) showErrorPopup()
            }

            isMoveNav.observe(viewLifecycleOwner) {
                when (it) {

                    // 일정 화면 이동
                    SCHEDULE -> (activity as MainActivity).binding.bottomNavView.selectedItemId =
                        R.id.calendar

                    // 메모 화면 이동
                    MEMO -> (activity as MainActivity).binding.bottomNavView.selectedItemId =
                        R.id.memo

                    // 지역 선택 화면 이동
                    WEATHER -> {
                        sharedViewModel.moveLocationSettingScreen.value = true
                        (activity as MainActivity).binding.bottomNavView.selectedItemId =
                            R.id.setting
                    }

                    WEEK -> {
                        navigate(R.id.weekWeatherFragment,
                            Bundle().apply {
                                putParcelableArrayList("week", viewModel.weekWeatherList.value)
                            }
                        )
                    }

                    _HOURLY -> {
                        navigate(R.id.hourlyWeatherFragment,
                            Bundle().apply {
                                putParcelableArrayList("rain", viewModel.hourlyRainList.value)
                                putParcelableArrayList("wind", viewModel.hourlyWindList.value)
                                putParcelableArrayList("hum", viewModel.hourlyHumList.value)
                            }
                        )
                    }

                    NEWS -> {
                        navigate(R.id.naverNewsFragment,
                            Bundle().apply {
                                putParcelableArrayList("news", viewModel.naverNews.value)
                            })
                    }
                }
            }

            loading.observe(viewLifecycleOwner) {
                sharedViewModel.isLoading.value = it
            }
        }

        binding.run {

            rvExchange.run {
                adapter = object : BaseRecyclerAdapter.Create<ExchangeModel, ItemExchangeBinding>(
                    R.layout.item_exchange,
                    bindingVariableId = BR.itemExchange,
                    bindingListener = BR.exchangeItemListener
                ){
                    override fun onItemClick(item: ExchangeModel, view: View) {}

                    override fun onItemLongClick(item: ExchangeModel, view: View): Boolean {
                        return true
                    }
                }
            }

            rvSchedule.run {
                adapter = object : BaseRecyclerAdapter.Create<CalendarModels, ItemCalendarBinding>(
                    R.layout.item_calendar,
                    bindingVariableId = BR.itemCalendar,
                    bindingListener = BR.calendarItemListener
                ) {
                    override fun onItemClick(item: CalendarModels, view: View) {}

                    override fun onItemLongClick(item: CalendarModels, view: View): Boolean {
                        return true
                    }
                }
            }

            rvMemo.run {
                adapter = object : BaseRecyclerAdapter.Create<MemoModels, ItemMemoBinding>(
                    R.layout.item_memo,
                    bindingVariableId = BR.itemMemo,
                    bindingListener = BR.memoListener
                ) {
                    override fun onItemLongClick(item: MemoModels, view: View): Boolean {
                        return true
                    }

                    override fun onItemClick(item: MemoModels, view: View) {
                        sharedViewModel.selectMemoId.value = item.id_
                        (activity as MainActivity).binding.bottomNavView.selectedItemId = R.id.memo
                    }
                }
            }

            rvHourlyWeather.run {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = object :
                    BaseRecyclerAdapter.Create<HourlyWeatherModel.Weather, ItemHourlyWeatherBinding>(
                        R.layout.item_hourly_weather,
                        bindingVariableId = BR.itemHourlyWeather,
                        bindingListener = BR.hourlyWeatherItemListener
                    ) {

                    override fun onItemLongClick(
                        item: HourlyWeatherModel.Weather,
                        view: View
                    ): Boolean {
                        return true
                    }
                    override fun onItemClick(item: HourlyWeatherModel.Weather, view: View) {}
                }
            }

            rvRealtimeTop10.run {
                adapter = object :
                    BaseRecyclerAdapter.Create<RealTimeModel.Top10, ItemRealtimeTop10Binding>(
                        R.layout.item_realtime_top10,
                        bindingVariableId = BR.itemRealTimeTop10,
                        bindingListener = BR.realTimeTop10ItemListener
                    ) {
                    override fun onItemLongClick(item: RealTimeModel.Top10, view: View): Boolean {
                        return true
                    }
                    override fun onItemClick(item: RealTimeModel.Top10, view: View) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(NAVER_SEARCH_URL + item.keyword)
                            )
                        )
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
                    override fun onItemLongClick(
                        item: RealTimeModel.Articles,
                        view: View
                    ): Boolean {
                        return true
                    }
                    override fun onItemClick(item: RealTimeModel.Articles, view: View) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
                    }
                }
            }
        }
    }

    private fun showErrorPopup() {
        OneButtonPopup().apply {
            showPopup(
                requireContext(),
                requireContext().getString(R.string.notice),
                requireContext().getString(R.string.internet_error),
                showAd = false
            )
            setDialogListener(object : OneButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {
                    sharedViewModel.isLoading.value = false
                    (activity as MainActivity).finish()
                }
            })
        }
    }
}