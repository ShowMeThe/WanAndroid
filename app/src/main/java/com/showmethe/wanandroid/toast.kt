package com.showmethe.wanandroid

import com.showmethe.wanandroid.base.WanApplication
import com.showmethe.wanandroid.constant.HAS_LOGIN
import com.showmethe.wanandroid.ui.auth.LoginActivity
import showmethe.github.core.base.AppManager
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.base.BaseApplication
import showmethe.github.core.base.ContextProvider
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.toast.ToastFactory

/**
 * com.ken.materialwanandroid
 *
 * 2019/9/4
 **/

fun toast(error: Int, message:String){
    ToastFactory.createToast(message)
    if(error == -1001){
        offline()
    }
}


fun offline(){
    AppManager.get().finishTarget(LoginActivity::class.java)
    val activity = ContextProvider.get().getActivity() as BaseActivity<*, *>
    if(WanApplication.lastActivity == null){
        WanApplication.lastActivity = ContextProvider.get().getActivity()?.javaClass
        WanApplication.lastBundle = ContextProvider.get().getActivity()?.intent?.extras
    }
    activity.startActivity(null, LoginActivity::class.java)
    RDEN.put("sessionId","")
    RDEN.put(HAS_LOGIN,false)
    RetroHttp.get().headerInterceptor.update("")
}