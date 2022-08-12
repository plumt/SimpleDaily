package com.yun.simpledaily.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.model.RealTimeModel
import com.yun.simpledaily.databinding.FragmentNaverNewsBinding
import com.yun.simpledaily.databinding.ItemNewsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class NaverNewsFragment
    :
    BaseBindingFragment<FragmentNaverNewsBinding, NaverNewsViewModel>(NaverNewsViewModel::class.java) {
    override val viewModel: NaverNewsViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_naver_news
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.naverNews

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.hideBottomView()

        arguments.run {
            this?.getParcelableArrayList<RealTimeModel.Naver>("news")?.run {
                viewModel.naverNewsList.value = this
            }
        }

        binding.apply {
            rvNews.run {
                adapter = object : BaseRecyclerAdapter.Create<RealTimeModel.Naver, ItemNewsBinding>(
                    R.layout.item_news,
                    bindingVariableId = BR.itemNews,
                    bindingListener = BR.newsListener
                ) {
                    override fun onItemClick(item: RealTimeModel.Naver, view: View) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
                    }

                    override fun onItemLongClick(item: RealTimeModel.Naver, view: View): Boolean {
                        return true
                    }
                }
            }
        }
    }
}