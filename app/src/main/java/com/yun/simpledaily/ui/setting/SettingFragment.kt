package com.yun.simpledaily.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.Constant.LOCATION_SETTING
import com.yun.simpledaily.data.model.SettingModel
import com.yun.simpledaily.databinding.FragmentSettingBinding
import com.yun.simpledaily.databinding.ItemSettingBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SettingFragment
    : BaseBindingFragment<FragmentSettingBinding, SettingViewModel>(SettingViewModel::class.java){
    override val viewModel: SettingViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_setting
    override fun initData(): Boolean = true
    override fun onBackEvent() { }
    override fun setVariable(): Int = BR.setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {

            rvSetting.run {
                adapter = object : BaseRecyclerAdapter.Create<SettingModel.Setting, ItemSettingBinding>(
                    R.layout.item_setting,
                    bindingVariableId = BR.itemSetting,
                    bindingListener = BR.settingListener
                ){
                    override fun onItemClick(item: SettingModel.Setting, view: View) {
                        when(item.title){
                            LOCATION_SETTING -> navigate(R.id.action_settingFragment_to_locationFragment)
                        }
                    }
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.showBottomView()
    }
}