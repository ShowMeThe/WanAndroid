package com.showmethe.wanandroid.base

import android.os.Bundle
import androidx.annotation.Keep
import com.showmethe.galley.database.DataSourceBuilder
import com.showmethe.wanandroid.api.auth
import com.showmethe.wanandroid.api.main

import com.showmethe.wanandroid.modules.AuthModules
import com.showmethe.wanandroid.modules.MainModules

import showmethe.github.core.base.BaseApplication
import showmethe.github.core.kinit.startInit
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
        startInit {
            modules(
                AuthModules(auth::class.java),
                MainModules(main::class.java)
            )
        }

    }

}