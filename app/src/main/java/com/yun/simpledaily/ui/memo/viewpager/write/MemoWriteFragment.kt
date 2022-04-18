package com.yun.simpledaily.ui.memo.viewpager.write

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.databinding.FragmentMemoWriteBinding
import com.yun.simpledaily.ui.memo.MemoViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MemoWriteFragment
    : BaseBindingFragment<FragmentMemoWriteBinding, MemoWriteViewModel>(MemoWriteViewModel::class.java){
    override val viewModel: MemoWriteViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_memo_write
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.memoWrite

    val viewPagerFragment: MemoViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

        }

        viewPagerFragment.apply {
            isSaveButtonClick.observe(viewLifecycleOwner){
                if(screen.value == Constant.MEMO_WRITE_SCREEN && it){
                    viewModel.insertMemo()
                    isSaveButtonClick.value = false
                }
            }
        }

    }
}