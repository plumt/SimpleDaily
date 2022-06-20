package com.yun.simpledaily.ui.memo.viewpager.write

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.model.MemoModel
import com.yun.simpledaily.data.repository.DB
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class MemoWriteViewModel(
    application: Application,
    private val db: DB
) : BaseViewModel(application) {

    val etTitle = MutableLiveData("")
    val etMemo = MutableLiveData("")
    val isWriteSuccess = MutableLiveData(false)

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
                isWriteSuccess.value = true
                clearMemo()
                Toast.makeText(
                    mContext,
                    mContext.getString(R.string.toast_save),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearMemo() {
        etTitle.value = ""
        etMemo.value = ""
    }
}