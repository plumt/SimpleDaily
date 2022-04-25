package com.yun.simpledaily.ui.location

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.Constant.ALL_LOCATION
import com.yun.simpledaily.data.Constant.FIRST_SCREEN
import com.yun.simpledaily.data.Constant.STEP_1
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.model.LocationModel
import com.yun.simpledaily.data.repository.api.Api
import kotlinx.coroutines.launch

class LocationViewModel(
    application: Application,
    private val api: Api
) : BaseViewModel(application){

    val LocationList = ListLiveData<LocationModel.Items>()

    val loading = MutableLiveData(false)

    val screen = MutableLiveData(FIRST_SCREEN)

    val subTitle = MutableLiveData(STEP_1)

//    https://juso.dev/docs/about/
//    https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=*00000000           모든 시, 도
//    https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=26*000000           군, 구
//    https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=2632*&is_ignore_zero=true       // 동

    init {
        callAddressApi()
    }

    fun callAddressApi(pattern: String = ALL_LOCATION, ignore: Boolean = false) {
        loading.value = true
        viewModelScope.launch {
            try {
                (callApi(
                    api.allAddress(pattern,ignore)
                ) as LocationModel.RS).run {
                    Log.d(TAG, "result : ${this.regcodes}")
                    this.regcodes?.run {
                        if (pattern != ALL_LOCATION && !ignore) {
                            this.removeAt(0)
                        }
                        LocationList.value = setId(this, 0)
                    }
                    loading.value = false
                }
            } catch (e: Throwable) {
                Log.e(TAG, "error : ${e.message}")
//                errorType.value = INTERNET_ERROR
                loading.value = false
            }
        }
    }

}