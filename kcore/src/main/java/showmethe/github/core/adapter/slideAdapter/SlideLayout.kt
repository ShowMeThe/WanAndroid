package showmethe.github.core.adapter.slideAdapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter.slideAdapter
 */
class SlideLayout constructor(context: Context, attrs: AttributeSet? = null) : HorizontalScrollView(context, attrs) {

    private var mRightMenuWidth: Int = 0
    var isOpen: Boolean = false
        private set


    val adapter: SlideAdapter<*>
        get() {
            var view: View = this
            while (true) {
                view = view.parent as View
                if (view is RecyclerView) {
                    break
                }
            }
            return (view as RecyclerView).adapter as SlideAdapter<*>
        }


    private var scrollingItem: SlideLayout?
        get() = adapter.scrollingItem
        set(scrollingItem) {
            adapter.scrollingItem = scrollingItem
        }

    private var downTime: Long = 0

    private var mCustomOnClickListener: CustomOnClickListener? = null

    fun setRightMenuWidth(rightMenuWidth: Int) {
        mRightMenuWidth = rightMenuWidth
    }


    private fun onOpenMenu() {
        adapter.holdOpenItem(this)
    }

    fun closeOpenMenu() {
        if (!isOpen) {
            adapter.closeOpenItem()
        }
    }


    fun openRightMenu() {
        isOpen = true
        this.smoothScrollBy(mRightMenuWidth, 0)
        onOpenMenu()
    }

    init {
        overScrollMode = View.OVER_SCROLL_NEVER
        isHorizontalScrollBarEnabled = false
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        scrollTo(0, 0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        closeOpenMenu()
        if (scrollingItem != null && scrollingItem !== this) {
            return false
        }
        scrollingItem = this
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downTime = System.currentTimeMillis()
                closeOpenMenu()
                scrollingItem = this
            }

            MotionEvent.ACTION_UP -> {
                scrollingItem = null
                val scrollX = scrollX
                if (System.currentTimeMillis() - downTime <= 100 && scrollX == 0) {
                    mCustomOnClickListener?.onClick()
                    return false
                }

                if (scrollX >= 0 && scrollX <= mRightMenuWidth / 2) {
                    close()
                }
                if (scrollX > mRightMenuWidth / 2) {
                    openRightMenu()
                }
                return false
            }
        }
        return super.onTouchEvent(ev)
    }

    fun close() {
        isOpen = false
        this.smoothScrollTo(0, 0)
    }


    internal interface CustomOnClickListener {
        fun onClick()
    }

    internal fun setCustomOnClickListener(listener: CustomOnClickListener) {
        this.mCustomOnClickListener = listener
    }


}
