package com.yun.simpledaily.ui.setting

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.Constant.DAILY_BOARED_SETTING
import com.yun.simpledaily.data.Constant.LOCATION_SETTING
import com.yun.simpledaily.data.Constant.RESET
import com.yun.simpledaily.data.model.SettingModel
import com.yun.simpledaily.util.PreferenceManager
import java.io.File

class SettingViewModel(
    application: Application,
    private val sharedPreferences: PreferenceManager
) : BaseViewModel(application) {

    val settingList = ListLiveData<SettingModel.Setting>()
    val title = arrayListOf(DAILY_BOARED_SETTING, LOCATION_SETTING, RESET)

    val isReset = MutableLiveData(false)

    init {
        addSetting()
    }

    fun clearAppData() {
        val cache: File = mContext.cacheDir //캐시 폴더 호출
        val appDir = File(cache.parent!!) //App Data 삭제를 위해 캐시 폴더의 부모폴더까지 호출
        if (appDir.exists()) {
            val children: Array<String> = appDir.list()!!
            for (s in children) {
                //App Data 폴더의 리스트를 deleteDir 를 통해 하위 디렉토리 삭제
                deleteDir(File(appDir, s))
            }
        }
        isReset.value = true
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children: Array<String> = dir.list()!!

            //파일 리스트를 반복문으로 호출
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }

        //디렉토리가 비어있거나 파일이므로 삭제 처리
        return dir!!.delete()
    }

    private fun addSetting() {
        title.forEachIndexed { index, s ->
            settingList.add(SettingModel.Setting(index, 0, s))
        }
    }
}