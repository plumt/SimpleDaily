package com.yun.simpledaily.data.model

import com.yun.simpledaily.base.Item

class BoardModel {
    data class Board(
        override var id: Int,
        override var viewType: Int,
        var title: String,
        var use: Boolean
    ) : Item()
}