package com.yun.simpledaily.ui.memo.viewpager.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.Constant.MEMO_DETAIL_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.databinding.FragmentMemoDetailBinding
import com.yun.simpledaily.ui.memo.MemoViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MemoDetailFragment :
BaseBindingFragment<FragmentMemoDetailBinding, MemoDetailViewModel>(MemoDetailViewModel::class.java){
    override val viewModel: MemoDetailViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_memo_detail
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.memoDetail

    val viewPagerFragment: MemoViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

        }

        viewPagerFragment.apply {

            isSaveButtonClick.observe(viewLifecycleOwner){
                if(screen.value == MEMO_DETAIL_SCREEN && it){
                    viewModel.updateMemo()
                    isSaveButtonClick.value = false
                }
            }

            selectMemo.observe(viewLifecycleOwner){
                viewModel.selectMemo.value = it
                viewModel.etMemo.value = it.memo
                viewModel.etTitle.value = it.title
            }

            updateMode.observe(viewLifecycleOwner){
                viewModel.updateMode.value = it
            }
        }

    }
}