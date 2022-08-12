package com.yun.simpledaily.ui.setting

import android.os.Bundle
import android.view.View
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant.DAILY_BOARED_SETTING
import com.yun.simpledaily.data.Constant.LOCATION_SETTING
import com.yun.simpledaily.data.Constant.RESET
import com.yun.simpledaily.data.model.SettingModel
import com.yun.simpledaily.databinding.FragmentSettingBinding
import com.yun.simpledaily.databinding.ItemSettingBinding
import com.yun.simpledaily.ui.popup.OneButtonPopup
import com.yun.simpledaily.ui.popup.TwoButtonPopup
import org.koin.android.viewmodel.ext.android.viewModel

class SettingFragment
    : BaseBindingFragment<FragmentSettingBinding, SettingViewModel>(SettingViewModel::class.java) {
    override val viewModel: SettingViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_setting
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.run {
            moveLocationSettingScreen.observe(viewLifecycleOwner) {
                if (it) {
                    moveLocationSettingScreen.value = false
                    navigate(R.id.action_settingFragment_to_locationFragment)
                }
            }
        }

        viewModel.run {

            isReset.observe(viewLifecycleOwner) {
                if (it) {
                    isReset.value = false
                    showOnePopup()
                }
            }
        }

        binding.run {

            rvSetting.run {
                adapter =
                    object : BaseRecyclerAdapter.Create<SettingModel.Setting, ItemSettingBinding>(
                        R.layout.item_setting,
                        bindingVariableId = BR.itemSetting,
                        bindingListener = BR.settingListener
                    ) {
                        override fun onItemLongClick(
                            item: SettingModel.Setting,
                            view: View
                        ): Boolean {
                            return true
                        }
                        override fun onItemClick(item: SettingModel.Setting, view: View) {
                            when (item.title) {
                                DAILY_BOARED_SETTING -> navigate(R.id.action_settingFragment_to_boardSettingFragment)
                                LOCATION_SETTING -> navigate(R.id.action_settingFragment_to_locationFragment)
                                RESET -> showResetPopup()
                            }
                        }
                    }
            }
        }
    }

    private fun showResetPopup() {
        TwoButtonPopup().apply {
            showPopup(
                requireContext(),
                requireContext().getString(R.string.notice),
                requireContext().getString(R.string.reset),
                requireContext().getString(R.string.cancel),
                requireContext().getString(R.string.btn_reset)
            )
            setDialogListener(object : TwoButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {
                    if (result) {
                        sharedViewModel.searchLocation.value = ""
                        viewModel.clearAppData()
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
                requireContext().getString(R.string.toast_reset),
                true
            )
            setDialogListener(object : OneButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {}
            })
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.showBottomView()
    }
}