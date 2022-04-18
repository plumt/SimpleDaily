package com.yun.simpledaily.ui.memo

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.Constant.MEMO
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.model.MemoModel
import com.yun.simpledaily.data.model.MemoModels
import com.yun.simpledaily.data.repository.DB
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.jsoup.Jsoup
import java.lang.Exception

class MemoViewModel(
    application: Application,
    private val db: DB
) : BaseViewModel(application){

    val memoList = ListLiveData<MemoModels>()
    val screen = MutableLiveData(MEMO_LIST_SCREEN)
    val selectMemo = MutableLiveData<MemoModels>()

    val updateMode = MutableLiveData(false)

    val isSaveButtonClick = MutableLiveData(false)

    fun selectMemoList(){
        viewModelScope.async {
            try {
                val list = arrayListOf<MemoModels>()
                launch(newSingleThreadContext(MEMO)) {
                    db.memoDao().selectMemo().forEachIndexed { index, memoModel ->
                        list.add(MemoModels(index,0, memoModel.id, memoModel.title, memoModel.memo))
                    }
                }.join()
                memoList.value = list
                Log.d(TAG,"test : ${memoList.value}")
            } catch (e: Exception){
                Log.e(TAG,"${e.message}")
                e.printStackTrace()
            }


        }



    }
}