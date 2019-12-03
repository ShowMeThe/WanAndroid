package com.showmethe.wanandroid.modules

import com.showmethe.wanandroid.api.auth
import com.showmethe.wanandroid.api.main
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.kinit.Module
import showmethe.github.core.kinit.getClassName

/**
 *  com.showmethe.wanandroid.modules
 *  2019/12/3
 *  23:31
 */
class AuthModules(private var cls:Class<auth>) : Module<auth>() {

    override fun moduleName(): String  = getClassName<auth>()

    override fun injectModule(): Lazy<auth> = lazy { RetroHttp.createApi(cls) }
}

class MainModules(private var cls:Class<main>) : Module<main>() {

    override fun moduleName(): String  = getClassName<main>()

    override fun injectModule(): Lazy<main> = lazy { RetroHttp.createApi(cls) }

}

