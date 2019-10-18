package showmethe.github.core.util.widget

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import showmethe.github.core.glide.TGlide

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.widget
 */

fun View.setOnSingleClickListner(onSingleClick : (it:View)->Unit){
    var lastClickTime = 0L
    val lastLongTime = 2000L

    setOnClickListener {
        val time = System.currentTimeMillis()
        if(time - lastClickTime > lastLongTime){
            onSingleClick.invoke(it)
            lastClickTime = time
        }
    }
}

fun ImageView.load(resource:Any){
    TGlide.load(resource,this)
}

fun ImageView.loadBackground(resource:Any){
    TGlide.loadInBackground(resource,this)
}


/**
 * 反射获取mLayoutScreen 设置对象为true,全屏效果，覆盖状态栏
 * @param needFullScreen
 */
fun fitPopWindowOverStatusBar(mPopupWindow: PopupWindow?, needFullScreen: Boolean) {
    try {
        val mLayoutInScreen = PopupWindow::class.java.getDeclaredField("mLayoutInScreen")
        mLayoutInScreen.isAccessible = true
        mLayoutInScreen.set(mPopupWindow, needFullScreen)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



fun getDisplayMetrics(context: Context): DisplayMetrics {
    val metric = DisplayMetrics()
    (context as Activity).windowManager.defaultDisplay.getMetrics(metric)
    return metric
}