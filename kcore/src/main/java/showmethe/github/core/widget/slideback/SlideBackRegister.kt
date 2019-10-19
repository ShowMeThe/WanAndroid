package showmethe.github.core.widget.slideback

import android.app.Activity
import android.app.Application
import android.os.Bundle

import showmethe.github.core.widget.slideback.annotation.SlideBackBinder

/**
 * 新增注解注册功能
 * Author: showMeThe
 * Update Time: 2019/10/20
 * Package Name:showmethe.github.core.widget.slideback
 */
class SlideBackRegister : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        register(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        unRegister(activity)
    }


    private fun register(activity: Activity) {
        val clazz = activity.javaClass
        if (clazz.isAnnotationPresent(SlideBackBinder::class.java)) {
            SlideBack.with(activity)
                .edgeAnimMode(SlideBack.ANIM_ROTATE)
                .callBack(activity::finishAfterTransition)
                .register()
        }
    }

    private fun unRegister(activity: Activity) {
        val clazz = activity.javaClass
        if (clazz.isAnnotationPresent(SlideBackBinder::class.java)) {
            SlideBack.unregister(activity)
        }
    }

}
