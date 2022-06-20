package com.yun.simpledaily.ui.memo.viewpager.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.data.Constant.MEMO_DETAIL_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_GO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.databinding.FragmentMemoDetailBinding
import com.yun.simpledaily.ui.memo.MemoViewModel
import com.yun.simpledaily.ui.popup.OneButtonPopup
import com.yun.simpledaily.ui.popup.TwoButtonPopup
import org.koin.android.viewmodel.ext.android.viewModel

class MemoDetailFragment :
    BaseBindingFragment<FragmentMemoDetailBinding, MemoDetailViewModel>(MemoDetailViewModel::class.java) {
    override val viewModel: MemoDetailViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_memo_detail
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.memoDetail

    private val viewPagerFragment: MemoViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerFragment.apply {

            sharedViewModel.memoScreen.observe(viewLifecycleOwner) {
                if (it == MEMO_GO_LIST_SCREEN && screen.value == MEMO_DETAIL_SCREEN) moveCheck()
            }

            isBackButtonCLick.observe(viewLifecycleOwner) {
                if (screen.value == MEMO_DETAIL_SCREEN && it) moveCheck()
            }

            isSaveButtonClick.observe(viewLifecycleOwner) {
                if (screen.value == MEMO_DETAIL_SCREEN && it) {
                    if (viewModel.etTitle.value != "" && viewModel.etMemo.value != "") {
                        viewModel.updateMemo()
                        isSaveButtonClick.value = false
                        updateMode.value = false
                    } else showOnePopup()
                }
            }

            isDeleteButtonClick.observe(viewLifecycleOwner) {
                if (it) {
                    viewModel.deleteMemo(screen)
                    isDeleteButtonClick.value = false
                    updateMode.value = false
                }
            }

            selectMemo.observe(viewLifecycleOwner) {
                viewModel.selectMemo.value = it
                viewModel.etMemo.value = it.memo
                viewModel.etTitle.value = it.title
            }

            updateMode.observe(viewLifecycleOwner) {
                viewModel.updateMode.value = it
            }
        }
    }

    private fun moveCheck() {
        viewModel.run {
            if (etMemo.value == selectMemo.value!!.memo &&
                etTitle.value == selectMemo.value!!.title
            ) goMemoListScreen()
            else showTwoPopup()
        }
    }

    private fun goMemoListScreen() {
        viewPagerFragment.screen.value = MEMO_LIST_SCREEN
        viewPagerFragment.updateMode.value = false
    }

    private fun showTwoPopup() {
        TwoButtonPopup().apply {
            showPopup(
                requireContext(),
                requireContext().getString(R.string.notice),
                requireContext().getString(R.string.back_question),
                secondBtn = requireContext().getString(R.string.memo_back)
            )
            setDialogListener(object : TwoButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {
                    if (result) goMemoListScreen()
                }
            })
        }
    }

    private fun showOnePopup() {
        OneButtonPopup().apply {
            showPopup(
                requireContext(),
                requireContext().getString(R.string.notice),
                requireContext().getString(R.string.save_question),
                true
            )
            setDialogListener(object : OneButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {}
            })
        }
    }
}