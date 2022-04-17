package com.yun.simpledaily.data.model

import com.yun.simpledaily.base.Item

class LocationModel {
    data class RS(
        val regcodes: ArrayList<Items>?
    )

    data class Items(
        override var id: Int = 0,
        override var viewType: Int = 0,
        val code: String,
        var name: String
    ) : Item()
}