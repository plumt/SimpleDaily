package com.yun.simpledaily.data.model

import com.yun.simpledaily.base.Item

class SettingModel {
    data class Setting(
        override var id: Int,
        override var viewType: Int = 0,
        val title: String
    ) : Item()
}