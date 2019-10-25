package showmethe.github.core.util.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 *  showmethe.github.core.util.widget
 *  2019/10/25
 *  9:44
 */
class ScrollHidFaBBehavior(context: Context, attrs: AttributeSet) : ScrollAwareFab(context, attrs) {

    override fun onScrollUp(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        child.show()

    }

    override fun onScrollDown(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        child.hide()

    }
}