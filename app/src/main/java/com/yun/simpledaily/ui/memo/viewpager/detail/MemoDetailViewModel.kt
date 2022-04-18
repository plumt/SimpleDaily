package com.yun.simpledaily.ui.memo.viewpager.detail

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.model.MemoModel
import com.yun.simpledaily.data.model.MemoModels
import com.yun.simpledaily.data.repository.DB
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.lang.Exception

class MemoDetailViewModel(
    application: Application,
    private val db: DB
) : BaseViewModel(application){

    val selectMemo = MutableLiveData<MemoModels>()

    val updateMode = MutableLiveData(false)

    val etTitle = MutableLiveData("")
    val etMemo = MutableLiveData("")

    fun updateMemo(){
        selectMemo.value!!.apply {
            memo = etMemo.value!!
            title = etTitle.value!!
        }
        viewModelScope.async {
            try {
                launch(newSingleThreadContext(Constant.MEMO)) {
                    db.memoDao().updateMemo(MemoModel(selectMemo.value!!.id_,selectMemo.value!!.title,selectMemo.value!!.memo))
                }.join()
                Toast.makeText(mContext,"수정하였습니다.",Toast.LENGTH_SHORT).show()

            } catch (e: Exception){
                Log.e(Constant.TAG,"${e.message}")
                e.printStackTrace()
            }


        }

    }
}