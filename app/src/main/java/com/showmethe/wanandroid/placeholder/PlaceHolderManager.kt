package com.showmethe.wanandroid.placeholder

import android.util.ArrayMap
import android.view.View

/**
 *  com.example.ken.kmvvm.placeholder
 *  2019/11/10
 *  20:57
 */
class PlaceHolderManager {

    private val views = ArrayMap<View,PlaceHolder>()

    fun patchViews(vararg varView: View){
        for(view in varView){
            val placeHolder=   PlaceHolder(view)
            placeHolder.onPatchView()
            views[view] = placeHolder
        }
    }

    fun clear(){
        if(views.isNotEmpty()){
            for(it in views){
                it.value.clear()
            }
        }
    }

}