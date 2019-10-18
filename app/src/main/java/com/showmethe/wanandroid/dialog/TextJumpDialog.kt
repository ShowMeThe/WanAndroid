package com.showmethe.wanandroid.dialog

import android.os.Bundle
import android.view.Gravity


import com.showmethe.wanandroid.R
import showmethe.github.core.dialog.SimpleDialogFragment
import showmethe.github.core.dialog.WindowParam

@WindowParam(gravity = Gravity.CENTER)
class TextJumpDialog : SimpleDialogFragment() {
    override fun build(savedInstanceState: Bundle?) {
        buildDialog {
            R.layout.dialog_text_jumo
        }
    }
}