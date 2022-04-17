package com.yun.simpledaily.di

import com.yun.simpledaily.data.repository.api.Api
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    fun providerSignalApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
    fun providerLocationApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    single(named("signal")) { providerSignalApi(get(named("signal"))) }
    single(named("location")) { providerLocationApi(get(named("location"))) }
}