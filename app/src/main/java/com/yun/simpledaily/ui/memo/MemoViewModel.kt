package com.yun.simpledaily.ui.memo

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.Constant.MEMO
import com.yun.simpledaily.data.Constant.MEMO_DETAIL_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.model.MemoModels
import com.yun.simpledaily.data.repository.DB
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class MemoViewModel(
    application: Application,
    private val db: DB
) : BaseViewModel(application){

    val memoList = ListLiveData<MemoModels>()
    val screen = MutableLiveData(MEMO_LIST_SCREEN)
    val selectMemo = MutableLiveData<MemoModels>()

    val updateMode = MutableLiveData(false)

    val isSaveButtonClick = MutableLiveData(false)
    val isDeleteButtonClick = MutableLiveData(false)
    val isBackButtonCLick = MutableLiveData(false)

    fun selectMemoList(selectId: Long){
        viewModelScope.async {
            try {
                val list = arrayListOf<MemoModels>()
                launch(newSingleThreadContext(MEMO)) {
                    val data = db.memoDao().selectMemo()
                    data.forEachIndexed { index, memoModel ->
                        list.add(MemoModels(index,0, memoModel.id, memoModel.title, memoModel.memo, last = data.size - 1))
                        if(memoModel.id == selectId){
                            selectMemo.postValue(list[index])
                        }
                    }
                }.join()
                memoList.value = list
                if(selectId != -1L){
                    screen.value = MEMO_DETAIL_SCREEN
                }
            } catch (e: Exception){
                Log.e(TAG,"${e.message}")
                e.printStackTrace()
            }
        }
    }
}