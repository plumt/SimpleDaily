package com.yun.simpledaily.ui.popup

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.yun.simpledaily.BR
import com.yun.simpledaily.R
import com.yun.simpledaily.databinding.DialogEditTextBinding

class EditTextPopup {
    lateinit var customDialogListener: CustomDialogListener

    fun showPopup(
        context: Context, title: String, contents: String, firstBtn: String = context.getString(
            R.string.cancel
        ), secondBtn: String = context.getString(R.string.result)
    ) {
        AlertDialog.Builder(context).run {
            setCancelable(true)
            val view = View.inflate(context, R.layout.dialog_edit_text, null)
            val binding = DialogEditTextBinding.bind(view)
            var memo = ""
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

            view.findViewById<EditText>(R.id.et_contents)
                .addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        memo = s.toString()
                        if (memo.trim() == "") {
                            view.findViewById<MaterialButton>(R.id.btn_two).run {
                                isEnabled = false
                                setBackgroundColor(resources.getColor(R.color.color_979797, null))
                            }
                        } else {
                            view.findViewById<MaterialButton>(R.id.btn_two).run {
                                isEnabled = true
                                setBackgroundColor(resources.getColor(R.color.color_FBA53B, null))
                            }
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }
                })

            // 확인 버튼
            view.findViewById<MaterialButton>(R.id.btn_two).setOnClickListener {
                customDialogListener.onResultClicked(true, memo)
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

    interface CustomDialogListener {
        fun onResultClicked(result: Boolean, memo: String = "")
    }

    fun setDialogListener(customDialogListener: CustomDialogListener) {
        this.customDialogListener = customDialogListener
    }
}