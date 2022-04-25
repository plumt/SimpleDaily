package com.yun.simpledaily.ui.popup

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.material.button.MaterialButton
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.databinding.DialogOneButtonBinding

class OneButtonPopup {
    lateinit var customDialogListener: CustomDialogListener

    fun showPopup(context: Context, title: String, contents: String, cancelable: Boolean = false, showAd: Boolean = true){
        AlertDialog.Builder(context).run {
            setCancelable(cancelable)
            val view = View.inflate(context, R.layout.dialog_one_button, null)
            val binding = DialogOneButtonBinding.bind(view)
            binding.setVariable(BR.title, title)
            binding.setVariable(BR.contents, contents)
            binding.setVariable(BR.btn_text, context.getString(R.string.result))

            binding.setVariable(BR.ads, showAd)

            setView(binding.root)
            val dialog = create()
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setOnDismissListener {
                customDialogListener.onResultClicked(true)
            }

            setAds(context, view.findViewById(R.id.my_template))

            // 확인 버튼
            view.findViewById<MaterialButton>(R.id.btn_result).setOnClickListener {
                customDialogListener.onResultClicked(true)
                dialog.dismiss()
            }
            dialog
        }.show()
    }

    private fun setAds(context: Context, templateView: TemplateView){
        val adLoader = AdLoader.Builder(context, context.getString(R.string.admob_native_test_id))
            .forNativeAd { ad: NativeAd ->
                // Show the ad.
                templateView.setNativeAd(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    // Methods in the NativeAdOptions.Builder class can be
                    // used here to specify individual options settings.
                    .build()
            )
            .build()
    }

    interface CustomDialogListener{
        fun onResultClicked(result: Boolean)
    }

    fun setDialogListener(customDialogListener: CustomDialogListener){
        this.customDialogListener = customDialogListener
    }
}