package com.yun.simpledaily.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.navigation.findNavController
import com.trello.rxlifecycle4.components.support.RxFragment
import com.yun.simpledaily.ui.main.MainViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment : RxFragment(), LifecycleListener {

    val mContext by lazy {
        context as Context
    }

    var mToast: Toast? = null

    val sharedViewModel: MainViewModel by sharedViewModel()

    @LayoutRes
    abstract fun getResourceId(): Int

    abstract fun onBackEvent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.addObserver(this)
        return inflater.inflate(getResourceId(), container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.run {
            hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    fun navigate(resId: Int, bundle: Bundle? = null) {
        try {
            bundle?.apply {
                view?.findNavController()?.navigate(resId, this)
            } ?: view?.findNavController()?.navigate(resId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showToast(message: String) {
        mToast = mToast?.apply {
            setText(message)
        } ?: run {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
        }
        mToast?.show()
    }

    override fun onUiStart() {
    }

    override fun onUiResume() {
    }

    override fun onUiPause() {
    }

    override fun onUiStop() {
    }
}