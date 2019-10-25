package showmethe.github.core.util.widget

import android.content.Context
import android.os.Build
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.widget
 */

class ScrollAwareFABBehavior(context: Context, attrs: AttributeSet) :
    ScrollAwareFab(context, attrs) {
    private var mIsAnimatingOut = false

    override fun onScrollDown(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onScrollDown(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed
        )
        if(!mIsAnimatingOut  && child.visibility == View.VISIBLE){
            animateOut(child)
        }
    }


    override fun onScrollUp(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onScrollUp(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed
        )

        if(child.visibility == View.INVISIBLE){
            animateIn(child)
        }
    }


    // Same animation that FloatingActionButton.Behavior uses to hide the FAB when the AppBarLayout exits
    private fun animateOut(button: FloatingActionButton) {
        if (Build.VERSION.SDK_INT >= 22) {
            ViewCompat.animate(button).translationX((button.width + getMarginBottom(button)).toFloat())
                .setInterpolator(INTERPOLATOR).withLayer()
                .setListener(object : ViewPropertyAnimatorListener {
                    override fun onAnimationStart(view: View) {
                        this@ScrollAwareFABBehavior.mIsAnimatingOut = true
                    }

                    override fun onAnimationCancel(view: View) {
                        this@ScrollAwareFABBehavior.mIsAnimatingOut = false
                    }

                    override fun onAnimationEnd(view: View) {
                        this@ScrollAwareFABBehavior.mIsAnimatingOut = false
                        view.visibility = View.INVISIBLE
                    }
                }).start()
        } else {
            button.hide()
        }
    }

    // Same animation that FloatingActionButton.Behavior uses to show the FAB when the AppBarLayout enters
    private fun animateIn(button: FloatingActionButton) {
        button.show()
        if (Build.VERSION.SDK_INT >= 22) {
            ViewCompat.animate(button).translationX(0f)
                .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                .start()
        } else {
            button.show()
        }
    }

    private fun getMarginBottom(v: View): Int {
        var marginBottom = 0
        val layoutParams = v.layoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            marginBottom = layoutParams.bottomMargin
        }
        return marginBottom
    }

    companion object {
        private val INTERPOLATOR = LinearInterpolator()
    }
}
