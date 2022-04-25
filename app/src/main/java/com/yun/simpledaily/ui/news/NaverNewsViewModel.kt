package com.yun.simpledaily.ui.news

import android.app.Application
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.model.RealTimeModel

class NaverNewsViewModel(
    application: Application
) : BaseViewModel(application){

    val naverNewsList = ListLiveData<RealTimeModel.Naver>()
}