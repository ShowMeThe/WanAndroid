package com.showmethe.wanandroid.util

import android.util.Log
import android.view.View

class HoldViewHelper {

    companion object{

        private val instant : HoldViewHelper by lazy { HoldViewHelper() }

        fun get() = instant

    }

    private var view: View? = null

    fun holdView(view: View){
        this.view = view
    }
    fun setHoldViewAlpha(alphaF:Float){
        Log.e("HoldViewHelper","Target View : $view")
        view?.apply {
            alpha = alphaF
        }
    }

    fun clear(){
        if(view !=null){
            view = null
        }
    }
}