package com.showmethe.wanandroid.expand

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
    enum class Slide{
        TOP,BOTTOM,LEFT,RIGHT
    }

    fun setExpandIcons(expands : ArrayList<ExpandIcon>) : Builder {
        this.expands = expands
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

    fun getSlide() = slide
    fun getExpandIcons() = expands

    fun build(){
        layout.setBuilder(this)
    }

}