package com.yun.simpledaily.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yun.simpledaily.R
import com.yun.simpledaily.data.Constant.NO_INTERNET
import com.yun.simpledaily.data.Constant.NO_UPDATE
import com.yun.simpledaily.data.Constant.REMOTE_KEY_APP_VERSION
import com.yun.simpledaily.data.Constant.TAG
import com.yun.simpledaily.ui.main.MainActivity
import com.yun.simpledaily.ui.popup.OneButtonPopup

class SplashActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 0 }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    private fun fetchAppVersion() {
        val appVersion = remoteConfig[REMOTE_KEY_APP_VERSION].asString()

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val server_app_version = appVersion.split('.')
                    val device_app_version =
                        this.packageManager.getPackageInfo(this.packageName, 0).versionName.split(
                            '.'
                        )
                    if (appVersion == "") {
                        // 서버에서 값 못가져옴
                        fetchAppVersion()
                        Log.d(TAG, "if -> 서버에서 값 못가져옴")
                    } else if (server_app_version[0] != device_app_version[0]
                        && server_app_version[1] != device_app_version[1]
                    ) {
                        // 메이저 및 마이너 업데이트
                        Log.d(TAG, "else if -> 업데이트 해야 함")
                        showPopup(
                            this@SplashActivity.getString(R.string.update_title),
                            this@SplashActivity.getString(
                                R.string.update_contents
                            ), NO_UPDATE
                        )
                    } else {
                        Log.d(TAG, "통과 ${server_app_version} : ${device_app_version}")
                        Handler().postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 1000)
                    }
                } else {
                    Log.d(TAG, it.exception.toString())
                    showPopup(
                        this@SplashActivity.getString(R.string.internet_title),
                        this@SplashActivity.getString(
                            R.string.internet_contents
                        ), NO_INTERNET
                    )
                }
            }
    }

    private fun showPopup(title: String, contents: String, flag: Int) {
        OneButtonPopup().apply {
            showPopup(
                this@SplashActivity,
                title,
                contents,
                showAd = false
            )
            setDialogListener(object : OneButtonPopup.CustomDialogListener {
                override fun onResultClicked(result: Boolean) {
                    when (flag) {
                        NO_INTERNET -> finish()
                        NO_UPDATE -> {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data =
                                Uri.parse(this@SplashActivity.getString(R.string.store_uri))
                            this@SplashActivity.startActivity(intent)
                        }
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        fetchAppVersion()
    }

    override fun onBackPressed() {}
}