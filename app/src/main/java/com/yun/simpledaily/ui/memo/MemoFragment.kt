package com.yun.simpledaily.ui.memo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.model.MemoModels
import com.yun.simpledaily.databinding.FragmentMemoBinding
import com.yun.simpledaily.databinding.ItemMemoBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MemoFragment
    : BaseBindingFragment<FragmentMemoBinding, MemoViewModel>(MemoViewModel::class.java) {
    override val viewModel: MemoViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_memo
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.memo

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
                        Toast.makeText(requireContext(),item.memo, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }
}