package showmethe.github.core.widget.slideback

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import showmethe.github.core.util.widget.dp2px
import showmethe.github.core.widget.slideback.SlideBack.Companion.ANIM_CLASSIC
import showmethe.github.core.widget.slideback.SlideBack.Companion.ANIM_ROTATE
import showmethe.github.core.widget.slideback.SlideBack.Companion.EDGE_BOTH
import showmethe.github.core.widget.slideback.SlideBack.Companion.EDGE_LEFT
import showmethe.github.core.widget.slideback.SlideBack.Companion.EDGE_RIGHT
import java.lang.ref.WeakReference
import showmethe.github.core.widget.slideback.widget.SlideBackIconView
import showmethe.github.core.widget.slideback.widget.SlideBackInterceptLayout
import kotlin.math.abs

/**
 * 修改自https://github.com/ParfoisMeng/SlideBack
 * Author: showMeThe
 * Update Time: 2019/10/20
 * Package Name:showmethe.github.core.widget.slideback
 */
class SlideBackManager  constructor(activity: Activity) {

    private var    slideBackIconViewLeft: SlideBackIconView? = null
    private var slideBackIconViewRight: SlideBackIconView? = null

    private var activity: WeakReference<Activity>? = null
    private var haveScroll: Boolean = false

    private var callBack: (()->Unit)? = null
    private var callBackWithState: ((mode:Int)->Unit)? = null

    private var backViewHeight = 0f // 控件高度
    private var arrowSize = 0f // 箭头图标大小
    private var maxSlideLength = 0f // 最大拉动距离

    private var sideSlideLength  = 0f // 侧滑响应距离
    private var dragRate  = 0f // 阻尼系数

    private var isAllowEdgeLeft   = false // 使用左侧侧滑
    private var isAllowEdgeRight  = false // 使用右侧侧滑

    private val screenWidth: Float // 屏幕宽

    private var animMode = ANIM_CLASSIC

    init {
        this.activity = WeakReference(activity)
        haveScroll = true

        // 获取屏幕信息，初始化控件设置
        val dm = activity.resources.displayMetrics
        screenWidth = dm.widthPixels.toFloat()

        backViewHeight = dm.heightPixels / 4f // 高度默认 屏高/4
        arrowSize = dp2px(activity,5f) // 箭头大小默认 5dp
        maxSlideLength = screenWidth / 12 // 最大宽度默认 屏宽/12

        sideSlideLength = maxSlideLength / 2 // 侧滑响应距离默认 控件最大宽度/2
        dragRate = 3f // 阻尼系数默认 3

        // 侧滑返回模式 默认:左
        isAllowEdgeLeft = true
        isAllowEdgeRight = false
    }

    /**
     * 是否包含滑动控件 默认false
     */
    fun haveScroll(haveScroll: Boolean): SlideBackManager {
        this.haveScroll = haveScroll
        return this
    }



    /**
     * 回调 适用于新的左右模式
     */
    fun callBack(callBack: (()->Unit)): SlideBackManager {
        this.callBack = callBack
        return this
    }


    /**
     * 回调 适用于新的左右模式,带状态
     */
    fun callBackWithState(callBackWithState: (mode:Int)->Unit): SlideBackManager {
        this.callBackWithState = callBackWithState
        return this
    }


    /**
     * 控件高度 默认屏高/4
     */
    fun viewHeight(backViewHeightDP: Float): SlideBackManager {
        this.backViewHeight = dp2px(activity?.get()!!,backViewHeightDP)
        return this
    }

    /**
     * 箭头大小 默认5dp
     */
    fun arrowSize(arrowSizeDP: Float): SlideBackManager {
        this.arrowSize = dp2px(activity?.get()!!,arrowSizeDP)
        return this
    }

    /**
     * 最大拉动距离（控件最大宽度） 默认屏宽/12
     */
    fun maxSlideLength(maxSlideLengthDP: Float): SlideBackManager {
        this.maxSlideLength = dp2px(activity?.get()!!,maxSlideLengthDP)
        return this
    }

    /**
     * 侧滑响应距离 默认控件最大宽度/2
     */
    fun sideSlideLength(sideSlideLengthDP: Float): SlideBackManager {
        this.sideSlideLength = dp2px(activity?.get()!!,sideSlideLengthDP)
        return this
    }

    /**
     * 阻尼系数 默认3（越小越灵敏）
     */
    fun dragRate(dragRate: Float): SlideBackManager {
        this.dragRate = dragRate
        return this
    }

    /**
     * 边缘侧滑模式 默认左
     */
    fun edgeMode(@SlideBack.EdgeMode edgeMode: Int): SlideBackManager {
        when (edgeMode) {
            EDGE_LEFT -> {
                isAllowEdgeLeft = true
                isAllowEdgeRight = false
            }
            EDGE_RIGHT -> {
                isAllowEdgeLeft = false
                isAllowEdgeRight = true
            }
            EDGE_BOTH -> {
                isAllowEdgeLeft = true
                isAllowEdgeRight = true
            }
            else -> throw RuntimeException("未定义的边缘侧滑模式值：EdgeMode = $edgeMode")
        }
        return this
    }

    fun edgeAnimMode(@SlideBack.AnimMode animMode: Int): SlideBackManager {
        when (animMode) {
            ANIM_CLASSIC -> this.animMode = ANIM_CLASSIC
            ANIM_ROTATE -> this.animMode = ANIM_ROTATE
        }/* case ANIM_RUNNING:
                this.animMode = ANIM_RUNNING;
                break;*/
        return this
    }


    /**
     * 需要使用滑动的页面注册
     */
    @SuppressLint("ClickableViewAccessibility")
    fun register() {
        if (isAllowEdgeLeft) {
            // 初始化SlideBackIconView 左侧
            slideBackIconViewLeft = SlideBackIconView(activity?.get()!!)
            slideBackIconViewLeft?.backViewHeight = backViewHeight
            slideBackIconViewLeft?.setArrowSize(arrowSize)
            slideBackIconViewLeft?.setMaxSlideLength(maxSlideLength)
            slideBackIconViewLeft?.animMode = animMode
        }
        if (isAllowEdgeRight) {
            // 初始化SlideBackIconView - Right
            slideBackIconViewRight = SlideBackIconView(activity?.get()!!)
            slideBackIconViewRight?.backViewHeight = backViewHeight
            slideBackIconViewRight?.setArrowSize(arrowSize)
            slideBackIconViewRight?.setMaxSlideLength(maxSlideLength)
            // 右侧侧滑 需要旋转180°
            slideBackIconViewRight?.rotationY = 180f
        }

        // 获取decorView并设置OnTouchListener监听
        val container = activity?.get()!!.window.decorView as FrameLayout
        if (haveScroll) {
            val interceptLayout = SlideBackInterceptLayout(activity?.get()!!)
            interceptLayout.setSideSlideLength(sideSlideLength)
            addInterceptLayout(container, interceptLayout)
        }
        if (isAllowEdgeLeft) {
            container.addView(slideBackIconViewLeft)
        }
        if (isAllowEdgeRight) {
            container.addView(slideBackIconViewRight)
        }
        container.setOnTouchListener(object : View.OnTouchListener {
            private var isSideSlideLeft = false  // 是否从左边边缘开始滑动
            private var isSideSlideRight = false  // 是否从右边边缘开始滑动
            private var downX = 0f // 按下的X轴坐标
            private var moveXLength = 0f // 位移的X轴距离

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN // 按下
                    -> {
                        // 更新按下点的X轴坐标
                        downX = event.rawX

                        // 检验是否从边缘开始滑动，区分左右
                        if (isAllowEdgeLeft && downX <= sideSlideLength) {
                            isSideSlideLeft = true
                        } else if (isAllowEdgeRight && downX >= screenWidth - sideSlideLength) {
                            isSideSlideRight = true
                        }
                    }
                    MotionEvent.ACTION_MOVE // 移动
                    -> if (isSideSlideLeft || isSideSlideRight) { // 从边缘开始滑动
                        // 获取X轴位移距离
                        moveXLength = abs(event.rawX - downX)

                        val slideLength = (moveXLength / dragRate).coerceAtMost(maxSlideLength)
                        // 如果位移距离在可拉动距离内，更新SlideBackIconView的当前拉动距离并重绘，区分左右
                        if (isAllowEdgeLeft && isSideSlideLeft) {
                            slideBackIconViewLeft?.updateSlideLength(slideLength)
                        } else if (isAllowEdgeRight && isSideSlideRight) {
                            slideBackIconViewRight?.updateSlideLength(slideLength)
                        }

                        // 根据Y轴位置给SlideBackIconView定位
                        if (isAllowEdgeLeft && isSideSlideLeft) {
                            setSlideBackPosition(slideBackIconViewLeft!!, event.rawY.toInt())
                        } else if (isAllowEdgeRight && isSideSlideRight) {
                            setSlideBackPosition(slideBackIconViewRight!!, event.rawY.toInt())
                        }
                    }
                    MotionEvent.ACTION_UP // 抬起
                    -> {
                        // 是从边缘开始滑动 且 抬起点的X轴坐标大于某值(默认3倍最大滑动长度) 且 回调不为空
                        if ((isSideSlideLeft || isSideSlideRight) && moveXLength / dragRate >= maxSlideLength && null != callBack) {
                            // 区分左右
                            callBack?.invoke()
                            callBackWithState?.invoke(if (isSideSlideLeft) EDGE_LEFT else EDGE_RIGHT)
                        }

                        // 恢复SlideBackIconView的状态
                        if (isAllowEdgeLeft && isSideSlideLeft) {
                            slideBackIconViewLeft?.updateSlideLength(0f)
                        } else if (isAllowEdgeRight && isSideSlideRight) {
                            slideBackIconViewRight?.updateSlideLength(0f)
                        }

                        // 从边缘开始滑动结束
                        isSideSlideLeft = false
                        isSideSlideRight = false
                    }
                }
                return isSideSlideLeft || isSideSlideRight
            }
        })
    }

    /**
     * 页面销毁时记得解绑
     * 其实就是置空防止内存泄漏
     */
    @SuppressLint("ClickableViewAccessibility")
    fun unregister() {
        //        FrameLayout container = (FrameLayout) activity.getWindow().getDecorView();
        //        if (haveScroll) removeInterceptLayout(container);
        //        container.removeView(slideBackIconViewLeft);
        //        container.setOnTouchListener(null);

        activity = null
        callBack = null
        slideBackIconViewLeft = null
        slideBackIconViewRight = null
    }

    /**
     * 给根布局包上一层事件拦截处理Layout
     */
    private fun addInterceptLayout(
        decorView: ViewGroup,
        interceptLayout: SlideBackInterceptLayout
    ) {
        val rootLayout = decorView.getChildAt(0) // 取出根布局
        decorView.removeView(rootLayout) // 先移除根布局
        // 用事件拦截处理Layout将原根布局包起来，再添加回去
        interceptLayout.addView(
            rootLayout,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        decorView.addView(interceptLayout)
    }

    /**
     * 将根布局还原，移除SlideBackInterceptLayout
     */
    private fun removeInterceptLayout(decorView: ViewGroup) {
        val rootLayout = decorView.getChildAt(0) as FrameLayout // 取出根布局
        decorView.removeView(rootLayout) // 先移除根布局
        // 将根布局的第一个布局(原根布局)取出放回decorView
        val oriLayout = rootLayout.getChildAt(0)
        rootLayout.removeView(oriLayout)
        decorView.addView(oriLayout)
    }

    /**
     * 给SlideBackIconView设置topMargin，起到定位效果
     *
     * @param view     SlideBackIconView
     * @param position 触点位置
     */
    private fun setSlideBackPosition(view: SlideBackIconView, position: Int) {
        // 触点位置减去SlideBackIconView一半高度即为topMargin
        val topMargin = (position - view.backViewHeight / 2).toInt()
        val layoutParams = FrameLayout.LayoutParams(view.layoutParams)
        layoutParams.topMargin = topMargin
        view.layoutParams = layoutParams
    }

}