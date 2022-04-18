package com.yun.simpledaily.ui.memo.viewpager.write

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.model.MemoModel
import com.yun.simpledaily.data.repository.DB
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.lang.Exception

class MemoWriteViewModel(
    application: Application,
    private val db: DB
) : BaseViewModel(application) {

    val etTitle = MutableLiveData("")
    val etMemo = MutableLiveData("")

    fun insertMemo() {
        viewModelScope.async {
            try {
                launch(newSingleThreadContext(Constant.MEMO)) {
                    db.memoDao().insertMemo(
                        MemoModel(
                            title = etTitle.value!!,
                            memo = etMemo.value!!
                        )
                    )
                }.join()
                etTitle.value = ""
                etMemo.value = ""
                Toast.makeText(mContext, "작성하였습니다.", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e(Constant.TAG, "${e.message}")
                e.printStackTrace()
            }


        }
    }

}