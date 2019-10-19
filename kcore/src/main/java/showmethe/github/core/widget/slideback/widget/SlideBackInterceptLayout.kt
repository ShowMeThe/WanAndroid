package showmethe.github.core.widget.slideback.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout


class SlideBackInterceptLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var sideSlideLength = 0f // 边缘滑动响应距离


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return ev.action == MotionEvent.ACTION_DOWN && (ev.rawX <= sideSlideLength || ev.rawX >= width - sideSlideLength)
    }

    fun setSideSlideLength(sideSlideLength: Float) {
        this.sideSlideLength = sideSlideLength
    }
}