package com.yun.simpledaily.ui.location

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.base.BaseBindingFragment
import com.yun.simpledaily.base.BaseRecyclerAdapter
import com.yun.simpledaily.data.Constant
import com.yun.simpledaily.data.Constant.FIRST_SCREEN
import com.yun.simpledaily.data.Constant.SECOND_LOCATION
import com.yun.simpledaily.data.Constant.SECOND_SCREEN
import com.yun.simpledaily.data.Constant.SHARED_LOCATION_KEY
import com.yun.simpledaily.data.Constant.STEP_2
import com.yun.simpledaily.data.Constant.STEP_3
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.Constant.THIRD_SCREEN
import com.yun.simpledaily.data.model.LocationModel
import com.yun.simpledaily.databinding.FragmentLocationBinding
import com.yun.simpledaily.databinding.ItemLocationBinding
import com.yun.simpledaily.di.sharedPreferences
import com.yun.simpledaily.ui.home.HomeViewModel
import com.yun.simpledaily.ui.main.MainActivity
import com.yun.simpledaily.ui.popup.OneButtonPopup
import com.yun.simpledaily.util.PreferenceManager
import org.koin.android.viewmodel.ext.android.viewModel

class LocationFragment
    :
    BaseBindingFragment<FragmentLocationBinding, LocationViewModel>(LocationViewModel::class.java) {
    override val viewModel: LocationViewModel by viewModel()
    override fun getResourceId(): Int = R.layout.fragment_location
    override fun initData(): Boolean = true
    override fun onBackEvent() {}
    override fun setVariable(): Int = BR.location

    val sharedPreferences = PreferenceManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.hideBottomView()

        viewModel.apply {
            loading.observe(viewLifecycleOwner) {
                sharedViewModel.isLoading.value = it
            }
        }

        binding.apply {


            rvLocation.run {
                adapter =
                    object : BaseRecyclerAdapter.Create<LocationModel.Items, ItemLocationBinding>(
                        R.layout.item_location,
                        bindingVariableId = BR.itemLocation,
                        bindingListener = BR.locationListener
                    ) {
                        override fun onItemClick(item: LocationModel.Items, view: View) {
                            when (viewModel.screen.value) {
                                FIRST_SCREEN -> {
                                    viewModel.callAddressApi(
                                        item.code.substring(
                                            0,
                                            2
                                        ) + SECOND_LOCATION
                                    )
                                    viewModel.screen.value = SECOND_SCREEN
                                    viewModel.subTitle.value = STEP_2
                                }
                                SECOND_SCREEN -> {
                                    viewModel.callAddressApi(item.code.substring(0, 4) + "*", true)
                                    viewModel.screen.value = THIRD_SCREEN
                                    viewModel.subTitle.value = STEP_3
                                }
                                THIRD_SCREEN -> {
                                    sharedPreferences.setString(
                                        requireContext(),
                                        SHARED_LOCATION_KEY, item.name
                                    )
                                    sharedViewModel.searchLocation.value = item.name
                                    showPopup(
                                        requireContext().getString(R.string.notice),
                                        requireContext().getString(R.string.success_location),
                                        (activity as MainActivity)
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun showPopup(title: String, contents: String, activity: MainActivity) {
        OneButtonPopup().apply {
            showPopup(
                requireContext(),
                title,
                contents
            )
            setDialogListener(object : OneButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {
                    if (result) {
//                        activity.navController.navigate(R.id.action_global_settingFragment)
                        activity.navController.popBackStack()
                    }
                }
            })
        }
    }
}