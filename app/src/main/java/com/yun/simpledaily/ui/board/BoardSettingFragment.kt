package com.yun.simpledaily.ui.board

import android.os.Bundle
import android.util.Log
import android.view.View
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant.MEMO
import com.yun.simpledaily.data.Constant.NEWS
import com.yun.simpledaily.data.Constant.REAL_TIME
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.Constant.WEATHER
import com.yun.simpledaily.data.Constant._HOURLY
import com.yun.simpledaily.data.model.BoardModel
import com.yun.simpledaily.databinding.FragmentBoardSettingBinding
import com.yun.simpledaily.databinding.ItemBoardBinding
import com.yun.simpledaily.util.PreferenceManager
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ØBoardSettingFragment
    :
    BaseBindingFragment<FragmentBoardSettingBinding, BoardSettingViewModel>(BoardSettingViewModel::class.java) {
    override val viewModel: BoardSettingViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_board_setting
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.boardSetting

    val sharedPreferences: PreferenceManager by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.hideBottomView()
        binding.apply {
            rvBoard.run {
                adapter = object : BaseRecyclerAdapter.Create<BoardModel.Board, ItemBoardBinding>(
                    R.layout.item_board,
                    bindingVariableId = BR.itemBoard,
                    bindingListener = BR.boardListener
                ) {
                    override fun onItemClick(item: BoardModel.Board, view: View) {
                        viewModel.boardList.value!![item.id].use = !item.use
                        notifyItemChanged(item.id)
                        requireContext().run {
                            Log.d(TAG,item.title + "  " + getString(R.string.now_weather))
                            when (item.title) {
                                getString(R.string.now_weather) -> setSharedPreferences(WEATHER,item.use)
                                getString(R.string.hourly_weather) -> setSharedPreferences(_HOURLY,item.use)
                                getString(R.string.realtime_top10) -> setSharedPreferences(REAL_TIME,item.use)
                                getString(R.string.realtime_popular_news) -> setSharedPreferences(NEWS,item.use)
                                getString(R.string.currently_memo) -> setSharedPreferences(MEMO,item.use)
                            }
                        }
                    }
                }
            }
        }
    }
    private fun setSharedPreferences(key: String, value: Boolean) {
        sharedPreferences.setString(requireContext(), key,value.toString())
    }
}