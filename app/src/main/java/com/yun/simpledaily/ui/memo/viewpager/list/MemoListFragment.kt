package com.yun.simpledaily.ui.memo.viewpager.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.Constant.MEMO_DETAIL_SCREEN
import com.yun.simpledaily.data.model.MemoModels
import com.yun.simpledaily.databinding.FragmentMemoListBinding
import com.yun.simpledaily.databinding.ItemMemoBinding
import com.yun.simpledaily.ui.memo.MemoViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MemoListFragment
    : BaseBindingFragment<FragmentMemoListBinding, MemoListViewModel>(MemoListViewModel::class.java){
    override val viewModel: MemoListViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_memo_list
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.memoList

    val viewPagerFragment: MemoViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvMemo.run {
                adapter = object : BaseRecyclerAdapter.Create<MemoModels, ItemMemoBinding>(
                    R.layout.item_memo,
                    bindingVariableId = BR.itemMemo,
                    bindingListener = BR.memoListener
                ) {
                    override fun onItemClick(item: MemoModels, view: View) {
                        viewPagerFragment.screen.value = MEMO_DETAIL_SCREEN
                        viewPagerFragment.selectMemo.value = item
                    }
                }
            }
        }

        viewPagerFragment.apply {
            memoList.observe(viewLifecycleOwner){
                if(it.size > 0){
                    viewModel.memoList.value = it
                }
            }
        }

    }
}