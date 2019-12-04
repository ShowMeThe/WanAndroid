package com.showmethe.wanandroid.base

import android.os.Bundle
import com.showmethe.galley.database.DataSourceBuilder
import com.showmethe.wanandroid.api.auth
import com.showmethe.wanandroid.api.main


import showmethe.github.core.base.BaseApplication
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.kinit.Module
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
            Module{
                single<auth>{ RetroHttp.createApi(auth::class.java) }
            }
            Module{
                single<main> {  RetroHttp.createApi(main::class.java) }
            }
        }

    }

}