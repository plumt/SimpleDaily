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
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.databinding.DialogTwoButtonBinding

class TwoButtonPopup {
    lateinit var customDialogListener: CustomDialogListener

    fun showPopup(context: Context, title: String, contents: String, firstBtn: String = context.getString(R.string.cancel), secondBtn: String = context.getString(R.string.exit)){
        AlertDialog.Builder(context).run {
            setCancelable(true)
            val view = View.inflate(context, R.layout.dialog_two_button, null)
            val binding = DialogTwoButtonBinding.bind(view)
            binding.setVariable(BR.title, title)
            binding.setVariable(BR.contents, contents)
            binding.setVariable(BR.first_btn_text, firstBtn)
            binding.setVariable(BR.second_btn_text, secondBtn)
            setView(binding.root)
            val dialog = create()
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setOnDismissListener {
                customDialogListener.onResultClicked(false)
            }

            setAds(context, view.findViewById(R.id.my_template))


            // 종료 버튼
            view.findViewById<MaterialButton>(R.id.btn_two).setOnClickListener {
                customDialogListener.onResultClicked(true)
                dialog.dismiss()
            }
            // 취소 버튼
            view.findViewById<MaterialButton>(R.id.btn_one).setOnClickListener {
                customDialogListener.onResultClicked(false)
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