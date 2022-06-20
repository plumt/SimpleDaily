package com.yun.simpledaily.data.model

import com.yun.simpledaily.base.Item

data class ExchangeModel(
    override var id: Int,
    override var viewType: Int,
    val country: String,
    val unit: String,
    val money: String,
    val before: String,
    val fluctuate: String
) : Item(){

    fun color() : Boolean = fluctuate.contains("+")

    fun title() : String = if(id == 0) country else "$country($unit)"
}
