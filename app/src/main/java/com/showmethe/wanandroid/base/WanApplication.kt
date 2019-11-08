package com.showmethe.wanandroid.base

import android.os.Bundle
import androidx.annotation.Keep
import com.showmethe.galley.database.DataSourceBuilder

import showmethe.github.core.base.BaseApplication
import showmethe.github.core.widget.slideback.SlideBackRegister


/**
 * Author: showMeThe
 * Update Time: 2019/10/18 14:31
 * Package Name:com.showmethe.wanandroid.base
 */
class WanApplication : BaseApplication() {

    companion object{

        var lastActivity : Class<*>? = null
        var lastBundle : Bundle? = null


    }

    override fun onCreate() {
        super.onCreate()
        DataSourceBuilder.build(this)
        registerActivityLifecycleCallbacks(SlideBackRegister())
    }

}