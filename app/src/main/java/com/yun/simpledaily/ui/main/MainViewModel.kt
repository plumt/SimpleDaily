package com.yun.simpledaily.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.util.PreferenceManager


class MainViewModel(
    application: Application,
    private val sharedPreferences: PreferenceManager
) : BaseViewModel(application) {

    val isLoading = MutableLiveData(false)


}