package com.showmethe.wanandroid.expand

import androidx.annotation.ColorRes
import com.showmethe.wanandroid.R

/**
 * Author: showMeThe
 * Update Time: 2019/11/2
 * Package Name:com.showmethe.wanandroid.expand
 */
class ExpandManager {

    companion object{

        fun newBuilder() : Builder {
            return Builder()
        }

    }

}

class Builder{

    private var expands = ArrayList<ExpandIcon>()
    private lateinit var layout : ExpandMenuChildLayout
    private var slide = Slide.TOP
    private var motionColor = R.color.black
    private var motionIcon = -1
    enum class Slide{
        TOP,BOTTOM
    }

    fun setExpandIcons(expands : ArrayList<ExpandIcon>) : Builder {
        this.expands = expands
        return this
    }

    fun motion(@ColorRes motionColor:Int,motionIcon: Int) : Builder{
        this.motionColor = motionColor
        this.motionIcon = motionIcon
        return this
    }

    fun bindTarget(layout: ExpandMenuChildLayout) : Builder {
        this.layout = layout
        return this
    }

    fun setSlide(slide: Slide) : Builder {
        this.slide = slide
        return this
    }

    fun getMotionColor() = motionColor
    fun getMotionIcon() = motionIcon
    fun getSlide() = slide
    fun getExpandIcons() = expands

    fun build(){
        layout.setBuilder(this)
    }

}