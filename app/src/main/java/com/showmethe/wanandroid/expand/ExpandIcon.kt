package com.showmethe.wanandroid.expand

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

import com.showmethe.wanandroid.R

/**
 * Author: showMeThe
 * Update Time: 2019/11/2
 * Package Name:com.example.ken.kmvvm.expand
 */
class ExpandIcon {


    private var icon  = -1
    @ColorRes
    private var iconTint = R.color.white
    @ColorRes
    private var backgroundTint  = R.color.colorAccent
    private var textLabel = ""


    fun setIcon(icon: Int) : ExpandIcon {
        this.icon = icon
        return this
    }

    fun setIconTint(@ColorInt iconTint: Int) : ExpandIcon {
        this.iconTint = iconTint
        return this
    }

    fun setBackgroundTint(@ColorRes backgroundTint: Int) : ExpandIcon {
        this.backgroundTint = backgroundTint
        return this
    }

    fun setTextLabel(textLabel: String) : ExpandIcon {
        this.textLabel = textLabel
        return this
    }

    fun getIcon() = icon
    fun getIconTint() = iconTint
    fun getBackgroundTint() = backgroundTint
    fun getTextLabel() = textLabel

}