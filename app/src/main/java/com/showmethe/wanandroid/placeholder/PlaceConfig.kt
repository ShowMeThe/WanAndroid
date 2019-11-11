package com.showmethe.wanandroid.placeholder

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View

/**
 * com.showmethe.wanandroid.placeholder
 * 2019/11/11
 */
class PlaceConfig(var view:View){
    private var defaultColor = Color.parseColor("#eeeeee")
    private var lightColor = Color.parseColor("#f7f7f7")
    private var drawable:PlaceHolderDrawable? = null
    private var holder : PlaceHolder? = null

    fun setDefaultColor(defaultColor:Int){
        this.defaultColor = defaultColor
    }

    fun getDefaultColor() = defaultColor

    fun setLightColor(lightColor:Int){
        this.lightColor = lightColor
    }

    fun getLightColor() = lightColor


    fun getHolder() : PlaceHolder?{
        if(drawable == null){
            drawable = PlaceHolderDrawable(this)
            holder = PlaceHolder(view,drawable!!)
            holder?.onPatchView()
        }
        return holder
    }

}
