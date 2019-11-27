package com.showmethe.wanandroid.placeholder

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.showmethe.wanandroid.placeholder.PlaceHolderDrawable
import showmethe.github.core.util.extras.onGlobalLayout
import showmethe.github.core.util.extras.onPreDrawLayout

/**
 *  com.example.ken.kmvvm.placeholder
 *  2019/11/10
 *  19:29
 */
class PlaceHolder(
    private var view: View,
    private var drawable: PlaceHolderDrawable,
    private var holderWidth: Int = 0,
    private var holderHeight: Int = 0) {


    fun onPatchView() {
        view.onPreDrawLayout {
            var width = holderWidth
            var height = holderHeight
            width = if (width == 0) {
                (view.parent as ViewGroup).measuredWidth
            } else {
                view.measuredWidth
            }
            height = if (height == 0) {
                (view.parent as ViewGroup).measuredHeight
            } else {
                view.measuredHeight
            }
            drawable.setBounds(0, 0, width, height)
            drawable.setupAnimator()
            view.foreground = drawable
        }
    }

    fun clear() {
        view.foreground = null
    }


}