package com.showmethe.wanandroid.placeholder

import android.util.ArrayMap
import android.view.View
import java.lang.ref.SoftReference

/**
 *  com.example.ken.kmvvm.placeholder
 *  2019/11/10
 *  20:57
 */
class PlaceHolderManager {

    private val views = SoftReference<ArrayMap<View,PlaceConfig>>(ArrayMap())

    fun patchViews(vararg varView: View){
        for(view in varView){
            val config = PlaceConfig(view)
            config.getHolder()
            views.get()!![view] = config
        }
    }


    fun patchViews(vararg varView: PlaceConfig){
        for(config in varView){
            config.getHolder()
            views.get()!![config.view] = config
        }
    }

    fun clear(){
        if( views.get()!!.isNotEmpty()){
            for(it in  views.get()!!){
                it.value.getHolder()?.clear()
            }
        }
    }

}