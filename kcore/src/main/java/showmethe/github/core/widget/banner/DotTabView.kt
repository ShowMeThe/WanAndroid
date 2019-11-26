package showmethe.github.core.widget.banner

import android.content.Context

import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

import showmethe.github.core.util.widget.dip2px


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:54
 * Package Name:showmethe.github.core.widget.banner
 */
/**
 * ViewPage底部圆点导航点
 */
class DotTabView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(mContext, attrs, defStyleAttr) {

    private var viewPager2: ViewPager2? = null
    private var viewPager: ViewPager? = null
    private var mDotDis: Int = 0//小圆点的距离
    private var divideWidth: Int = 0
    private var selectRadius = default_radius
    private var unSelectRadius = default_select_radius
    private val selectDot: ImageView = ImageView(mContext)


    fun setViewPager2(
        viewPager: ViewPager2?,
        tabCount: Int,
        totalCount: Int,
        scrollType: Int,
        dWidth: Int,
        dotType: Int,
        selectColor: Int,
        unSelectColor: Int
    ) {
        if (viewPager == null) return
        this.viewPager2 = viewPager

        createView(tabCount, dWidth, selectColor, unSelectColor, dotType)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (scrollType == Banner.REPEAT) {
                    val params = selectDot
                        .layoutParams as LayoutParams
                    params.leftMargin = (mDotDis * positionOffset).toInt() + position * mDotDis
                    selectDot.layoutParams = params
                }

            }

            override fun onPageSelected(position: Int) {
                if (scrollType == Banner.INFINITY) {
                    val params = selectDot
                        .layoutParams as LayoutParams
                    if (position == totalCount - 1) {
                        params.leftMargin = -(mDotDis * position)
                    } else {
                        params.leftMargin = (position - 1) * mDotDis
                    }
                    selectDot.layoutParams = params
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


    fun setViewPager(
        viewPager: ViewPager?,
        tabCount: Int,
        dWidth: Int,
        selectColor: Int,
        unSelectColor: Int
    ) {
        if (viewPager == null) return
        this.viewPager = viewPager

        createView(tabCount, dWidth, selectColor, unSelectColor, 0)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val params = selectDot
                    .layoutParams as LayoutParams
                params.leftMargin = (mDotDis * positionOffset).toInt() + position * mDotDis
                selectDot.layoutParams = params
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


    private fun createView(
        tabCount: Int,
        dWidth: Int,
        selectColor: Int,
        unSelectColor: Int,
        dotType: Int
    ) {

        if (dWidth <= 0) {
            divideWidth = dip2px(mContext, 10f)
        } else {
            divideWidth = dip2px(mContext, dWidth.toFloat())
        }
        if (divideWidth < unSelectRadius) {
            divideWidth = (unSelectRadius * 1.5).toInt()
        }
        removeAllViews()
        init(mContext, tabCount, unSelectColor, dotType)


        val select = GradientDrawable()
        when (dotType) {
            0 -> {
                selectDot.minimumHeight = selectRadius
                selectDot.minimumWidth = selectRadius
                select.shape = GradientDrawable.OVAL
            }
            1 -> {
                selectDot.minimumHeight = selectRadius / 3
                selectDot.minimumWidth = selectRadius
                select.shape = GradientDrawable.RECTANGLE
                select.cornerRadius = selectRadius.toFloat() / 3
            }
        }

        select.setColor(selectColor)
        selectDot.setImageDrawable(select)
        val param = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )

        selectDot.layoutParams = param

        selectDot.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                selectDot.viewTreeObserver.removeOnGlobalLayoutListener(this)
                //计算得到小圆点距离
                mDotDis = getChildAt(1).left - getChildAt(0)
                    .left
            }
        })
        addView(selectDot, param)
    }


    fun setIndicatorRadius(dp1: Int, dp2: Int) {
        selectRadius = dp1
        unSelectRadius = dp2
    }


    private fun init(context: Context, tabCount: Int, unSelectColor: Int, dotType: Int) {

        for (i in 0 until tabCount) {
            val unSelectDot = ImageView(context)
            val unselect = GradientDrawable()

            when (dotType) {
                0 -> {
                    unSelectDot.minimumHeight = selectRadius
                    unSelectDot.minimumWidth = selectRadius
                    unselect.shape = GradientDrawable.OVAL
                }
                1 -> {
                    unSelectDot.minimumHeight = selectRadius / 3
                    unSelectDot.minimumWidth = selectRadius
                    unselect.shape = GradientDrawable.RECTANGLE
                    unselect.cornerRadius = selectRadius.toFloat() / 3
                }
            }
            unselect.setColor(unSelectColor)
            unSelectDot.setImageDrawable(unselect)
            val params = LayoutParams(
                LinearLayout
                    .LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            )
            if (i > 0) {
                params.leftMargin = divideWidth + (i - 1) * divideWidth//设置圆点边距
            }
            unSelectDot.layoutParams = params
            addView(unSelectDot, params)
        }
    }

    companion object {
        private val default_radius = 15
        private val default_select_radius = 16
    }

}
