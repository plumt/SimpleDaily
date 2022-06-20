package com.yun.simpledaily.ui.memo.viewpager.list

import android.app.Application
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.model.MemoModels

class MemoListViewModel(
    application: Application
) : BaseViewModel(application){
    val memoList = ListLiveData<MemoModels>()
}