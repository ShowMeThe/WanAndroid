package com.showmethe.wanandroid.placeholder

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.showmethe.wanandroid.placeholder.PlaceHolderDrawable

/**
 *  com.example.ken.kmvvm.placeholder
 *  2019/11/10
 *  19:29
 */
class PlaceHolder(private var view: View){


    fun onPatchView(){
        if(view.viewTreeObserver.isAlive){
            view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    val drawable = PlaceHolderDrawable(view)
                    var width = 0
                    var height = 0
                    width = if(view.measuredWidth == 0){
                        (view.parent as ViewGroup).measuredWidth
                    }else{
                        view.measuredWidth
                    }
                    height = if(view.measuredHeight == 0){
                        25
                    }else{
                        view.measuredHeight
                    }
                    drawable.setBounds(0,0,width,height)
                    drawable.setupAnimator()
                    view.foreground = drawable
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })
        }
    }

    fun clear(){
        view.foreground = null
    }


}