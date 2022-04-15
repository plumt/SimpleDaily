package com.yun.simpledaily.data.repository.api

import com.yun.simpledaily.data.model.RealTimeModel
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET("/news/realtime")
    suspend fun realtime(): Response<RealTimeModel.RS>
}