package showmethe.github.core.util.toast

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast


import showmethe.github.core.R
import showmethe.github.core.base.BaseApplication
import showmethe.github.core.base.ContextProvider

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.toast
 */
object ToastFactory {

    private var toast: Toast? = null

    private var imgToast: Toast? = null

    private val mHandler = Handler(Looper.getMainLooper())


    fun createToast(message: Any) {
        ContextProvider.get().getActivity()?.runOnUiThread {
            if (isNotificationEnabled(ContextProvider.get().context)) {
                createNormalToast(message)
            } else {
                createViewToast(message)
            }
        }
    }


    private fun createViewToast(message: Any) {
        if ( ContextProvider.get().getActivity() == null) {
            return
        }
        val container =  ContextProvider.get().getActivity()!!
                .findViewById<ViewGroup>(android.R.id.content)
        val mView = View.inflate( ContextProvider.get().getActivity(), R.layout.view_toast_draw, null)
        container.addView(mView)

        val title = mView.findViewById<TextView>(R.id.text)
        title.text = message.toString()

        // 显示动画
        val mFadeInAnimation = AlphaAnimation(0.0f, 1.0f)
        // 消失动画
        val mFadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
        mFadeOutAnimation.duration = 200
        mFadeOutAnimation
                .setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }
                    override fun onAnimationEnd(animation: Animation) {
                        // 隐藏布局，不使用remove方法为防止多次创建多个布局
                        mView.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })

        mView.visibility = View.VISIBLE
        mFadeInAnimation.duration = 300

        mView.startAnimation(mFadeInAnimation)
        mHandler.postDelayed({ mView.startAnimation(mFadeOutAnimation) }, 1500)
    }


    private fun createNormalToast(message: Any) {
        val title: TextView
        val layout: View
        if (toast == null) {
            layout = View.inflate(ContextProvider.get().context, R.layout.view_toast_draw, null)
        } else {
            toast?.cancel()
            layout = toast!!.view
        }
        title = layout.findViewById(R.id.text)
        title.text = message.toString()
        toast = Toast.makeText( ContextProvider.get().context, "", Toast.LENGTH_SHORT)
        toast?.apply {
            setGravity(Gravity.TOP, 0, 100)
            duration = Toast.LENGTH_SHORT
            setText(message.toString())
            view = layout
            show()
        }
    }


    fun createImgToast(isTrue: Boolean, message: Any) {
        val title: TextView

        val layout: View = if (imgToast == null) {
            View.inflate(ContextProvider.get().context, R.layout.view_img_toast, null)
        } else {
            imgToast?.cancel()
            imgToast!!.view
        }
        title = layout.findViewById(R.id.text)
        val imageView: ImageView = layout.findViewById(R.id.image)
        if (isTrue) {
            title.setTextColor(Color.parseColor("#3F75FF"))

        } else {
            title.setTextColor(Color.parseColor("#333333"))

        }
        title.text = message.toString()
        imgToast = Toast.makeText(ContextProvider.get().context, "", Toast.LENGTH_SHORT)
        imgToast?.apply {
            setGravity(Gravity.TOP, 0, 0)
            duration = Toast.LENGTH_SHORT
            setText(message.toString())
            view = layout
            show()
        }

    }


    /**
     * 检查通知栏权限有没有开启
     *
     */
    @SuppressLint("ObsoleteSdkInt")
    fun isNotificationEnabled(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).areNotificationsEnabled()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val appInfo = context.applicationInfo
            val pkg = context.applicationContext.packageName
            val uid = appInfo.uid

            try {
                val appOpsClass = Class.forName(AppOpsManager::class.java.name)
                val checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String::class.java)
                val opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION")
                val value = opPostNotificationValue.get(Int::class.java) as Int
                return checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) as Int == 0
            } catch (ignored: Exception) {
                return true
            }

        } else {
            return true
        }
    }
}
