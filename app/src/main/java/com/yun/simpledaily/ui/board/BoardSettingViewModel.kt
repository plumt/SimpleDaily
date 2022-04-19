package com.yun.simpledaily.ui.board

import android.app.Application
import android.util.Log
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseViewModel
import com.yun.simpledaily.base.ListLiveData
import com.yun.simpledaily.data.Constant.MEMO
import com.yun.simpledaily.data.Constant.NEWS
import com.yun.simpledaily.data.Constant.REAL_TIME
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.Constant.WEATHER
import com.yun.simpledaily.data.Constant._HOURLY
import com.yun.simpledaily.data.model.BoardModel
import com.yun.simpledaily.util.PreferenceManager

class BoardSettingViewModel(
    application: Application,
    private val sharedPreferences: PreferenceManager
) : BaseViewModel(application){

    val boardList = ListLiveData<BoardModel.Board>()

    private val titleList = arrayListOf(
        mContext.getString(R.string.now_weather),
        mContext.getString(R.string.hourly_weather),
        mContext.getString(R.string.realtime_top10),
        mContext.getString(R.string.realtime_popular_news),
        mContext.getString(R.string.currently_memo))

    init {
        Log.d(TAG,"init BoardSettingViewModel")
        Log.d(TAG,"0 : ${sharedPreferences.getString(mContext, WEATHER)}")
        Log.d(TAG,"1 : ${sharedPreferences.getString(mContext, _HOURLY)}")
        Log.d(TAG,"2 : ${sharedPreferences.getString(mContext, REAL_TIME)}")
        Log.d(TAG,"3 : ${sharedPreferences.getString(mContext, NEWS)}")
        Log.d(TAG,"4 : ${sharedPreferences.getString(mContext, MEMO)}")



        sharedPreferences.run {
            boardList.add(BoardModel.Board(0,0,titleList[0],getUse(WEATHER)))
            boardList.add(BoardModel.Board(1,0,titleList[1],getUse(_HOURLY)))
            boardList.add(BoardModel.Board(2,0,titleList[2],getUse(REAL_TIME)))
            boardList.add(BoardModel.Board(3,0,titleList[3],getUse(NEWS)))
            boardList.add(BoardModel.Board(4,0,titleList[4],getUse(MEMO)))
        }

    }

    private fun getUse(key: String) : Boolean = PreferenceManager.getString(mContext, key).toBoolean()
}