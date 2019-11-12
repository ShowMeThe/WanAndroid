package com.showmethe.speeddiallib.expand

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.showmethe.speeddiallib.R


/**
 * Author: showMeThe
 * Update Time: 2019/11/11 11:39
 * Package Name:com.showmethe.speeddiallib.expand
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