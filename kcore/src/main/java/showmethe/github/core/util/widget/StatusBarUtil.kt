package showmethe.github.core.util.widget

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import showmethe.github.core.util.system.GetSystemUtils


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.widget
 */

object StatusBarUtil {


    //OPPO
    private val SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010

    fun registerActivity(activity: Activity) {
        if (GetSystemUtils.system == GetSystemUtils.SYS_FLYME) {
            setMeizuStatusBarDarkIcon(activity, true)
        } else if (GetSystemUtils.system == GetSystemUtils.SYS_MIUI) {
            setMiuiStatusBarDarkMode(activity, true)
        } else if (GetSystemUtils.system == GetSystemUtils.SYS_OPPO) {
            setOPPOStatusTextColor(activity, true)
        } else {
            android6StatusBarDarkMode(activity, true)
        }


    }


    fun fixToolbarScreen(activity: Activity, toolbar: Toolbar) {
        setFullScreen(activity)
        val params = toolbar.layoutParams
        params.height = (getStatusBarHeight(activity) * 1).toInt()
        toolbar.layoutParams = params
    }


    fun fixToolbar(activity: Activity, toolbar: Toolbar) {
        val params = toolbar.layoutParams
        params.height = (getStatusBarHeight(activity) * 1).toInt()
        toolbar.layoutParams = params
    }

    /**
     * 顶部状态栏上移
     */
    fun setFullScreen(activity: Activity) {
        setScreen(activity)
        val mContentView = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            mChildView.fitsSystemWindows = false
        }
    }


    private fun setScreen(activity: Activity) {
        val window = activity.window
        val decorView = window.decorView
        //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }


    fun setStatusColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(activity, color)
        }
    }


    fun setTranslucentNavigation(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }


    fun setTranslucentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun setTranslucent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }


    fun addStatusBarView(activity: Activity, color: Int, isBlack: Boolean) {
        setScreen(activity)
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        val statusBarView = View(activity)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity))
        if (contentView.childCount > 1) {
            contentView.removeViewAt(1)
        }
        statusBarView.setBackgroundColor(ContextCompat.getColor(activity, color))
        contentView.addView(statusBarView, lp)
        if (isBlack) {
            registerActivity(activity)
        }
    }


    fun addTranslucentStatusBarView(activity: Activity, color: Int, isBlack: Boolean) {
        setFullScreen(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        val statusBarView = View(activity)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity))
        statusBarView.setBackgroundColor(ContextCompat.getColor(activity, color))
        contentView.addView(statusBarView, lp)
        if (isBlack) {
            registerActivity(activity)
        }
    }


    fun getStatusBarHeight(activity: Activity): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun setOPPOStatusTextColor(activity: Activity, lightStatusBar: Boolean) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var vis = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lightStatusBar) {
                vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis = vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (lightStatusBar) {
                vis = vis or SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT
            } else {
                vis = vis and SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT.inv()
            }
        }
        window.decorView.systemUiVisibility = vis
    }

    //3.对于flyme:
    //设置成白色的背景，字体颜色为黑色。
    private fun setMeizuStatusBarDarkIcon(activity: Activity?, dark: Boolean): Boolean {
        var result = false
        if (activity != null) {
            try {
                val lp = activity.window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                activity.window.attributes = lp
                result = true
            } catch (e: Exception) {
            }

        }
        return result
    }


    //2.对于miui
    //设置成白色的背景，字体颜色为黑色。
    private fun setMiuiStatusBarDarkMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            val clazz = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true
                //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark) {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }
            } catch (e: Exception) {
            }

        }
        return result
    }


    //1.对于android6.0，但是小米魅族不适配
    //设置成白色的背景，字体颜色为黑色。
    //需要在跟布局设置  android:fitsSystemWindows="true"
    //不设置上面的参数，布局会往上移
    private fun android6StatusBarDarkMode(activity: Activity, dark: Boolean) {
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}
