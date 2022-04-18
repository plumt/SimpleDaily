package com.yun.simpledaily.ui.memo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.data.Constant.MEMO_DETAIL_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_WRITE_SCREEN
import com.yun.simpledaily.databinding.FragmentMemoBinding
import com.yun.simpledaily.ui.memo.viewpager.detail.MemoDetailFragment
import com.yun.simpledaily.ui.memo.viewpager.list.MemoListFragment
import com.yun.simpledaily.ui.memo.viewpager.write.MemoWriteFragment
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

            imgAddMemo.setOnClickListener {
                viewModel.screen.value = MEMO_WRITE_SCREEN
            }

            tvDelete.setOnClickListener {
                Toast.makeText(requireContext(), requireContext().getString(R.string.toast_delete), Toast.LENGTH_SHORT).show()
                viewModel.updateMode.value = false
                viewModel.screen.value = MEMO_LIST_SCREEN
            }

            tvUpdate.setOnClickListener {
                viewModel.updateMode.value = !viewModel.updateMode.value!!
            }

            tvBack.setOnClickListener {
                viewModel.updateMode.value = false
                viewModel.screen.value = MEMO_LIST_SCREEN
            }

            binding.tvSave.setOnClickListener {
                Toast.makeText(requireContext(), requireContext().getString(R.string.toast_save), Toast.LENGTH_SHORT).show()
                viewModel.updateMode.value = false
                viewModel.isSaveButtonClick.value = true
                if(viewModel.screen.value == MEMO_WRITE_SCREEN){
                    viewModel.screen.value = MEMO_LIST_SCREEN
                }
            }

            vpMemo.run {
                isUserInputEnabled = false
                adapter = object : FragmentStateAdapter(this@MemoFragment) {
                    override fun getItemCount(): Int = 3
                    override fun createFragment(position: Int): Fragment {
                        return when (position) {
                            0 -> MemoWriteFragment()
                            1 -> MemoListFragment()
                            2 -> MemoDetailFragment()
                            else -> Fragment()
                        }
                    }
                }
                setCurrentItem(1,false)
            }


        }

        viewModel.apply {
            screen.observe(viewLifecycleOwner) {
                if (it == MEMO_LIST_SCREEN || it == MEMO_DETAIL_SCREEN || it == MEMO_WRITE_SCREEN) {
                    binding.vpMemo.setCurrentItem(it, true)
                }
                if(it == MEMO_LIST_SCREEN){
                    viewModel.selectMemoList()
                }
            }


        }

    }
}