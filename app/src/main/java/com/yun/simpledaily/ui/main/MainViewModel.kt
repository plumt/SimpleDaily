package com.yun.simpledaily.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.SHARED_LOCATION_KEY
import com.yun.simpledaily.util.PreferenceManager


class MainViewModel(
    application: Application,
    private val sharedPreferences: PreferenceManager
) : BaseViewModel(application) {

    val isLoading = MutableLiveData(false)
    val isBottomVisible = MutableLiveData(true)
    val searchLocation = MutableLiveData("")
    val moveLocationSettingScreen = MutableLiveData(false)
    val memoScreen = MutableLiveData(MEMO_LIST_SCREEN)
    val selectMemoId = MutableLiveData<Long>(-1)

    init {
        sharedPreferences.run {
            getString(mContext, SHARED_LOCATION_KEY)?.run {
                searchLocation.value = this
            }
        }
    }

    fun hideBottomView() {
        isBottomVisible.value = false
    }

    fun showBottomView() {
        isBottomVisible.value = true
    }
}