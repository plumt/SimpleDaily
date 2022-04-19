package com.yun.simpledaily.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.Constant.MEMO
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.NEWS
import com.yun.simpledaily.data.Constant.REAL_TIME
import com.yun.simpledaily.data.Constant.SHARED_LOCATION_KEY
import com.yun.simpledaily.data.Constant.WEATHER
import com.yun.simpledaily.data.Constant._HOURLY
import com.yun.simpledaily.data.model.MemoModels
import com.yun.simpledaily.util.PreferenceManager


class MainViewModel(
    application: Application,
    private val sharedPreferences: PreferenceManager
) : BaseViewModel(application) {

    val isLoading = MutableLiveData(false)
    val isBottomVisible = MutableLiveData(true)

    val searchLocation = MutableLiveData("")

    init {

        sharedPreferences.run {
            getString(mContext,SHARED_LOCATION_KEY)?.run {
                searchLocation.value = this
            }

            if(getString(mContext, WEATHER) == "") setString(mContext, WEATHER,"true")
            if(getString(mContext, _HOURLY) == "") setString(mContext, _HOURLY,"true")
            if(getString(mContext, REAL_TIME) == "") setString(mContext, REAL_TIME,"true")
            if(getString(mContext, NEWS) == "") setString(mContext, NEWS,"true")
            if(getString(mContext, MEMO) == "") setString(mContext, MEMO,"true")
        }

    }

    val memoScreen = MutableLiveData(MEMO_LIST_SCREEN)

    val selectMemoId = MutableLiveData<Long>(-1)


    fun hideBottomView(){
        isBottomVisible.value = false
    }

    fun showBottomView(){
        isBottomVisible.value = true
    }
}