package com.yun.simpledaily.ui.setting

import android.app.Application
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.Constant.DAILY_BOARED_SETTING
import com.yun.simpledaily.data.Constant.LOCATION_SETTING
import com.yun.simpledaily.data.model.SettingModel

class SettingViewModel(
    application: Application
) : BaseViewModel(application) {

    val settingList = ListLiveData<SettingModel.Setting>()
    val title = arrayListOf(DAILY_BOARED_SETTING, LOCATION_SETTING)

    init {
        addSetting()
    }


    private fun addSetting() {
        title.forEachIndexed { index, s ->
            settingList.add(SettingModel.Setting(index, 0, s))
        }
    }
}