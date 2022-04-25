package com.yun.simpledaily.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.yun.simpledaily.R
import com.yun.simpledaily.data.Constant.HOME
import com.yun.simpledaily.data.Constant.HOURLY_WEATHER
import com.yun.simpledaily.data.Constant.MEMO_GO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.MEMO_LIST_SCREEN
import com.yun.simpledaily.data.Constant.SCHEDULE
import com.yun.simpledaily.data.Constant.SETTING
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.data.Constant.WEEK_WEATHER
import com.yun.simpledaily.data.Constant._MEMO
import com.yun.simpledaily.data.Constant._NEWS
import com.yun.simpledaily.databinding.ActivityMainBinding
import com.yun.simpledaily.ui.popup.LoadingDialog
import com.yun.simpledaily.ui.popup.TwoButtonPopup
import com.yun.simpledaily.util.PreferenceManager
import org.koin.android.ext.android.inject

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

        binding.frPartyAd.run {
            setClientId("DAN-9Z6B0mj0mQGSd8bw")
            setAdListener(object : com.kakao.adfit.ads.AdListener{
                override fun onAdLoaded() {
                    // 호출 완료
                }

                override fun onAdClicked() {
                    // 광고 클릭시
                }

                override fun onAdFailed(p0: Int) {
                    // 호출 실패시
                }
            })
            loadAd()
        }

        binding.bottomNavView.run {
            this.setOnNavigationItemSelectedListener {
                if (navController.currentDestination?.label != it.title) {
                    when (it.title) {
                        HOME -> navController.navigate(R.id.action_global_homeFragment)
                        SCHEDULE -> navController.navigate(R.id.action_global_calendarFragment)
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
        navController.currentDestination?.let { nav ->

            when(nav.label){
                HOME, SCHEDULE, SETTING -> showExitPopup()
                _MEMO -> {
                    if (mainViewModel.memoScreen.value == MEMO_LIST_SCREEN) {
                        showExitPopup()
                    } else {
                        mainViewModel.memoScreen.value = MEMO_GO_LIST_SCREEN
                    }
                }
                HOURLY_WEATHER, WEEK_WEATHER, _NEWS -> navController.navigate(R.id.action_global_homeFragment)
                else -> super.onBackPressed()
            }



        } ?: super.onBackPressed()


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

    override fun onPause() {
        super.onPause()
        binding.frPartyAd.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.frPartyAd.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.frPartyAd.destroy()
    }
}