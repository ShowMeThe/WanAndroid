package showmethe.github.core.util.extras

import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.Window
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.circularreveal.CircularRevealCompat
import com.google.android.material.circularreveal.CircularRevealFrameLayout
import com.google.android.material.circularreveal.CircularRevealWidget
import kotlin.math.hypot

/**
 *  showmethe.github.core.util.extras
 *  2019/12/7
 *  11:09
 */

fun FragmentActivity.setReveal(savedInstanceState: Bundle?){
    if (savedInstanceState == null) {
        val container = window.decorView as FrameLayout
        val rootLayout = container.getChildAt(0) // 取出根布局
        container.removeView(rootLayout) // 先移除根布局
        val layout  = CircularRevealFrameLayout(this)
        layout.addView(rootLayout,ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        container.addView(layout)
        container.onPreDrawLayout {
            circularReveal(layout)
        }
    }
}


/**
 * 水波纹覆盖关闭消失动画
 * @param rootLayout
 */
fun circularReveal(rootLayout: View, call : ()->Unit) {
    val centerX = rootLayout.width.toFloat() /2
    val centerY = rootLayout.height.toFloat() /2
    val finalRadius = hypot(
        centerX.coerceAtLeast(rootLayout.width - centerX),
        centerY.coerceAtLeast(rootLayout.height - centerY)
    )
    val circularReveal = ViewAnimationUtils.createCircularReveal(
        rootLayout,
        (rootLayout.width * 0.5).toInt(),
        (rootLayout.height * 0.5).toInt(), finalRadius,0f
    )
    circularReveal.duration = 500
    circularReveal.interpolator = LinearInterpolator()
    rootLayout.postDelayed({
        call.invoke()
        rootLayout.alpha = 0.8f
    },250)
    circularReveal.start()
}



private fun circularReveal(rootLayout : CircularRevealFrameLayout){
    val centerX = rootLayout.width.toFloat() /2
    val centerY = rootLayout.height.toFloat() /2
    val finalRadius = hypot(
        centerX.coerceAtLeast(rootLayout.width - centerX),
        centerY.coerceAtLeast(rootLayout.height - centerY)
    )
    rootLayout.revealInfo = CircularRevealWidget.RevealInfo(centerX,centerY,0f)
    val circularReveal = CircularRevealCompat.createCircularReveal(rootLayout,centerX,centerY,finalRadius)
    circularReveal.duration = 500
    circularReveal.interpolator = LinearInterpolator()
    circularReveal.startDelay = 1
    circularReveal.start()
}