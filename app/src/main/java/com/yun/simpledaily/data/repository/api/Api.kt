package com.yun.simpledaily.data.repository.api

import com.yun.simpledaily.data.model.LocationModel
import com.yun.simpledaily.data.model.RealTimeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/news/realtime")
    suspend fun realtime(): Response<RealTimeModel.RS>

    @GET("/v1/regcodes")
    suspend fun allAddress(
        @Query("regcode_pattern") regcode_pattern: String,
        @Query("is_ignore_zero") is_ignore_zero: Boolean
    ) : Response<LocationModel.RS>
}