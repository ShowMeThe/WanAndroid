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
    private var width = 0
    private var height = 0


    fun setDefaultColor(defaultColor:Int) : PlaceConfig{
        this.defaultColor = defaultColor
        return this
    }

    fun getDefaultColor() = defaultColor

    fun setLightColor(lightColor:Int) : PlaceConfig{
        this.lightColor = lightColor
        return this
    }

    fun getLightColor() = lightColor

    fun setPlaceHolderSize(width:Int,height: Int): PlaceConfig {
        this.width = width
        this.height = height
        return this
    }


    fun getHolder() : PlaceHolder?{
        if(drawable == null){
            drawable = PlaceHolderDrawable(this)
            holder = PlaceHolder(view,drawable!!,width,height)
            holder?.onPatchView()
        }
        return holder
    }

}
