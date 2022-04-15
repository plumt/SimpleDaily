package com.yun.simpledaily.data.model

import com.tickaroo.tikxml.annotation.Element
import com.yun.simpledaily.base.Item

class RealTimeModel {
    data class RS(
        // 시그널 실시간 검색어
        val top10: ArrayList<Top10>,

        // 인기 뉴스
        val articles: ArrayList<Articles>,

        //언론사별 가장 많이 본 뉴스
        val naver: ArrayList<Naver>,

        val youtube: ArrayList<Youtube>

    )

    data class Top10(
        override var id: Int,
        override var viewType: Int = 0,
        val rank: Int,
        val keyword: String,
        val state: String
    ) : Item()

    data class Articles(
        override var id: Int,
        override var viewType: Int = 0,
        val name: String,
        val title: String,
        val image: String,
        val link: String,
        val desc: String
    ) : Item()

    data class Naver(
        override var id: Int,
        override var viewType: Int = 0,
        val link: String,
        val title: String,
        val image: String,
        val press: Press,
        val desc: String
    ) : Item() {
        data class Press(
            val name: String,
            val image: String
        )
    }

    data class Youtube(
        override var id: Int,
        override var viewType: Int = 0,
        val kind: String,
        val etag: String,
        @Element(name = "id")
        val youtube_id: Id,
        val snippet: Snippet
    ) : Item() {
        data class Id(
           val kind: String,
           val videoId: String
        )
        data class Snippet(
            val publishedAt: String,
            val channelId: String,
            val title: String,
            val description: String,
            val thumbnails: Thumbnails
        ){
            data class Thumbnails(
                val default: Details,
                val medium: Details,
                val high: Details
            ){
                data class Details(
                    val url: String,
                    val width: Int,
                    val height: Int
                )
            }
        }
    }
}