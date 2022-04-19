package com.yun.simpledaily.ui.memo.viewpager.write

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.Constant.MEMO_GO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_WRITE_SCREEN
import com.yun.simpledaily.databinding.FragmentMemoWriteBinding
import com.yun.simpledaily.ui.main.MainActivity
import com.yun.simpledaily.ui.memo.MemoViewModel
import com.yun.simpledaily.ui.popup.OneButtonPopup
import com.yun.simpledaily.ui.popup.TwoButtonPopup
import org.koin.android.viewmodel.ext.android.viewModel

class MemoWriteFragment
    :
    BaseBindingFragment<FragmentMemoWriteBinding, MemoWriteViewModel>(MemoWriteViewModel::class.java) {
    override val viewModel: MemoWriteViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_memo_write
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.memoWrite

    val viewPagerFragment: MemoViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerFragment.apply {

            sharedViewModel.memoScreen.observe(viewLifecycleOwner){
                if(it == MEMO_GO_LIST_SCREEN && screen.value == MEMO_WRITE_SCREEN){
                    moveCheck()
                }
            }

            isBackButtonCLick.observe(viewLifecycleOwner) {
                if (screen.value == MEMO_WRITE_SCREEN && it) {
                    moveCheck()
                }
            }

            isSaveButtonClick.observe(viewLifecycleOwner) {
                if (screen.value == MEMO_WRITE_SCREEN && it) {
                    if(viewModel.etMemo.value != "" && viewModel.etTitle.value != ""){
                        viewModel.insertMemo()
                        isSaveButtonClick.value = false
                        goMemoListScreen()
                    } else{
                        showOnePopup()
                    }
                }
            }
        }
    }

    private fun goMemoListScreen(){
        viewPagerFragment.screen.value = MEMO_LIST_SCREEN
        viewPagerFragment.updateMode.value = false
    }

    private fun moveCheck(){
        if (viewModel.etTitle.value == "" && viewModel.etMemo.value == "") {
            viewPagerFragment.screen.value = MEMO_LIST_SCREEN
            viewPagerFragment.updateMode.value = false
        } else {
            showTwoPopup()
        }
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
                    if (result) {
                        goMemoListScreen()
                        viewModel.clearMemo()
                    }
                }
            })
        }
    }

    private fun showOnePopup() {
        OneButtonPopup().apply {
            showPopup(
                requireContext(),
                requireContext().getString(R.string.notice),
                requireContext().getString(R.string.save_question)
            )
            setDialogListener(object : OneButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {
                    if (result) {

                    }
                }
            })
        }
    }
}