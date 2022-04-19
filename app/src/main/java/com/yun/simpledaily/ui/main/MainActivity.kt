package com.yun.simpledaily.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yun.simpledaily.R
import com.yun.simpledaily.data.Constant.CALENDAR
import com.yun.simpledaily.data.Constant.HOME
import com.yun.simpledaily.data.Constant.MEMO_GO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.data.Constant._MEMO
import com.yun.simpledaily.data.Constant.SETTING
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.databinding.ActivityMainBinding
import com.yun.simpledaily.ui.popup.LoadingDialog
import com.yun.simpledaily.ui.popup.TwoButtonPopup
import com.yun.simpledaily.util.PreferenceManager
import org.koin.android.ext.android.inject
import org.koin.ext.scope

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

    lateinit var dialog: LoadingDialog

    val sharedPreferences: PreferenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.main = mainViewModel

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        binding.bottomNavView.run {
            this.setOnNavigationItemSelectedListener {
                if (navController.currentDestination?.label != it.title) {
                    when (it.title) {
                        HOME -> navController.navigate(R.id.action_global_homeFragment)
                        CALENDAR -> navController.navigate(R.id.action_global_calendarFragment)
                        _MEMO -> navController.navigate(R.id.action_global_memoFragment)
                        SETTING -> navController.navigate(R.id.action_global_settingFragment)
                    }
                }
                true
            }
        }

        dialog = LoadingDialog(this)

        mainViewModel.isLoading.observe(this) {
            if (it) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        }

    }

    override fun onBackPressed() {
        if (navController.currentDestination?.label == HOME ||
            navController.currentDestination?.label == CALENDAR ||
            navController.currentDestination?.label == SETTING) {
            showExitPopup()
        } else if(navController.currentDestination?.label ==_MEMO){
            if(mainViewModel.memoScreen.value == MEMO_LIST_SCREEN){
                showExitPopup()
            } else{
                mainViewModel.memoScreen.value = MEMO_GO_LIST_SCREEN
            }
        }
        else{
            super.onBackPressed()
        }
    }

    private fun showExitPopup() {
        TwoButtonPopup().apply {
            showPopup(
                this@MainActivity,
                this@MainActivity.getString(R.string.notice),
                this@MainActivity.getString(R.string.exit_question)
            )
            setDialogListener(object : TwoButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {
                    if (result) {
                        finish()
                    }
                }
            })
        }
    }
}