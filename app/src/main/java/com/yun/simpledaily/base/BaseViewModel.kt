package com.yun.simpledaily.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.repository.api.ApiClass
import kotlinx.coroutines.async
import retrofit2.Response

open class BaseViewModel constructor(application: Application) : AndroidViewModel(application) {
    val mContext = application.applicationContext

//    val db = (application as PortfolioApplication).db

    val navigatorFlag = MutableLiveData<Int>()

    suspend fun callApi(api: Response<*>): Any? {
        var result: Any? = null
        viewModelScope.async {
            async { result = ApiClass(api).callApi() }.join()
        }.await()
        return result
    }

    fun <T : Item> setId(model: ArrayList<T>, plus: Int = 0): ArrayList<T> {
        model.forEachIndexed { index, t ->
            model[index].id = index + plus
        }
        return model
    }
}