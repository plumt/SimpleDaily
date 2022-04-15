package com.yun.simpledaily.util

import android.content.Context
import android.content.SharedPreferences
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object PreferenceManager {
    const val PREFERENCES_NAME = "portpolio"
    const val DEFAULT_VALUE_STRING = ""
    private const val DEFAULT_VALUE_BOOLEAN = false
    private const val DEFAULT_VALUE_INT = -1
    private const val DEFAULT_VALUE_LONG = -1L
    private const val DEFAULT_VALUE_FLOAT = -1f
    open fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun setString(context: Context, key: String?, value: String?) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun setInt(context: Context, key: String?, value: Int) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getString(context: Context, key: String?): String? {
        val prefs = getPreferences(context)
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }

    fun getInt(context: Context, key: String?): Int? {
        val prefs = getPreferences(context)
        return prefs.getInt(key, DEFAULT_VALUE_INT)
    }

    fun getAll(context: Context): MutableCollection<out Any?> {
        val prefs = getPreferences(context)
        return prefs.all.keys
    }
}

object Util {
    @BindingAdapter("setImages")
    @JvmStatic
    fun ImageView.setImages(path: String?) {
        this.run {
            Glide.with(context)
                .load(path)
                .into(this)
        }
    }

//    @BindingAdapter("setAds")
//    @JvmStatic
//    fun TemplateView.setAds(id: Int?) {
//        val adLoader = AdLoader.Builder(context, context.getString(R.string.admob_native_test_id))
//            .forNativeAd { ad: NativeAd ->
//                // Show the ad.
//                this.setNativeAd(ad)
//            }
//            .withAdListener(object : AdListener() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    // Handle the failure by logging, altering the UI, and so on.
//                }
//            })
//            .withNativeAdOptions(
//                NativeAdOptions.Builder()
//                    // Methods in the NativeAdOptions.Builder class can be
//                    // used here to specify individual options settings.
//                    .build()
//            )
//            .build()
//    }
}
