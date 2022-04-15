package com.yun.simpledaily.data.repository.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

class ApiClass<T : Response<*>>(val api: T) {
    suspend fun callApi(): Any? {
        var result: Any? = null
        CoroutineScope(Dispatchers.IO).async {
            if (api.isSuccessful) {
                result = api.body()!!
            }
        }.join()
        return result
    }
}