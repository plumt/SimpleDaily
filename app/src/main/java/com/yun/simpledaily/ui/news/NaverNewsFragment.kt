package com.yun.simpledaily.ui.news

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.model.HourlyWeatherModel
import com.yun.simpledaily.data.model.RealTimeModel
import com.yun.simpledaily.databinding.FragmentNaverNewsBinding
import com.yun.simpledaily.databinding.ItemNewsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class NaverNewsFragment
    : BaseBindingFragment<FragmentNaverNewsBinding, NaverNewsViewModel>(NaverNewsViewModel::class.java){
    override val viewModel: NaverNewsViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_naver_news
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.naverNews

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.hideBottomView()

        arguments.run {
            this?.getParcelableArrayList<RealTimeModel.Naver>("news")?.run {
                Log.d(TAG,"news : $this")
                viewModel.naverNewsList.value = this
            }
        }

        viewModel.naverNewsList.observe(viewLifecycleOwner){
            Log.d(TAG,"news : $it")
        }

        binding.apply {
            rvNews.run {
                adapter = object : BaseRecyclerAdapter.Create<RealTimeModel.Naver, ItemNewsBinding>(
                    R.layout.item_news,
                    bindingVariableId = BR.itemNews,
                    bindingListener = BR.newsListener
                ){
                    override fun onItemClick(item: RealTimeModel.Naver, view: View) {
                        Toast.makeText(requireContext(),item.link,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }
}