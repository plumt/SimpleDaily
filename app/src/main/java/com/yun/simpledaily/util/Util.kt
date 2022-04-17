package com.yun.simpledaily.util

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.yun.simpledaily.R
import com.yun.simpledaily.data.Constant.BAD
import com.yun.simpledaily.data.Constant.GOOD
import com.yun.simpledaily.data.Constant.NOMAL
import com.yun.simpledaily.data.Constant.WORST

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

    fun newsRank(rank: Int?): String {
        return if (rank == null) ""
        else "인기 ${rank + 1}위"
    }

    fun backColor(num: Int?): Int {
        return if (num == null) 1
        else when (num) {
            1, 4, 5, 8, 9 -> 0
            else -> 1
        }
    }

    fun dustCheck(str: String?): Int {
        return if (str == null) 0
        else when {
            str.contains("좋음") -> GOOD
            str.contains("보통") -> NOMAL
            str.contains("매우") -> WORST
            str.contains("나쁨") -> BAD
            else -> 0
        }
    }

    fun uvCheck(str: String?): Int {
        return if (str == null) 0
        else when {
//            str.contains("좋음") -> GOOD
//            str.contains("보통") -> NOMAL
            str.contains("매우") -> WORST
            str.contains("높음") -> BAD
            else -> 0
        }
    }


    @BindingAdapter("setImages")
    @JvmStatic
    fun ImageView.setImages(path: String?) {

        if (!path.isNullOrEmpty()) {
            GlideToVectorYou
                .init()
                .with(context)
                .setPlaceHolder(R.color.white, R.color.white)
                .load(Uri.parse(path), this);
        }
    }

    @BindingAdapter("setNewsImages")
    @JvmStatic
    fun ImageView.setNewsImages(path: String?) {
        this.run {
            Glide.with(context)
                .load(path)
                .centerCrop()
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
