package com.yun.simpledaily.ui.popup

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.google.android.material.button.MaterialButton
import com.yun.simpledaily.R
import com.yun.simpledaily.BR
import com.yun.simpledaily.databinding.DialogOneButtonBinding

class OneButtonPopup {
    lateinit var customDialogListener: CustomDialogListener

    fun showPopup(context: Context, title: String, contents: String){
        AlertDialog.Builder(context).run {
            setCancelable(false)
            val view = View.inflate(context, R.layout.dialog_one_button, null)
            val binding = DialogOneButtonBinding.bind(view)
            binding.setVariable(BR.title, title)
            binding.setVariable(BR.contents, contents)
            binding.setVariable(BR.btn_text, context.getString(R.string.result))
            setView(binding.root)
            val dialog = create()
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setOnDismissListener {
                customDialogListener.onResultClicked(true)
            }

            // 확인 버튼
            view.findViewById<MaterialButton>(R.id.btn_result).setOnClickListener {
                customDialogListener.onResultClicked(true)
                dialog.dismiss()
            }
            dialog
        }.show()
    }
    interface CustomDialogListener{
        fun onResultClicked(result: Boolean)
    }

    fun setDialogListener(customDialogListener: CustomDialogListener){
        this.customDialogListener = customDialogListener
    }
}