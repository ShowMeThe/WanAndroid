package showmethe.github.core.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import showmethe.github.core.glide.TGlide
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.util.extras.SimpleLifecycleCallbacks
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.system.crash.CrashHandler
import showmethe.github.core.util.system.startLocalForIp
import java.lang.ref.WeakReference

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
@Keep
open class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch(Dispatchers.IO) {
            startLocalForIp(this@BaseApplication)
            RetroHttp.get()
            RDEN.build(this@BaseApplication)
          //  CrashHandler.get(this@BaseApplication)
        }
        registerActivityLifecycleCallbacks(object : SimpleLifecycleCallbacks(){
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if(activity is AppCompatActivity){
                    ContextProvider.get().attach(activity)
                }
            }
            override fun onActivityResumed(activity: Activity?) {
                if(activity is AppCompatActivity){
                    ContextProvider.get().attach(activity)
                }
            }
        })
    }

}