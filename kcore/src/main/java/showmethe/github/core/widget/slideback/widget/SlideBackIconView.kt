package showmethe.github.core.widget.slideback.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

import androidx.annotation.ColorInt

/**
 * 修改自https://github.com/ParfoisMeng/SlideBack
 * Author: showMeThe
 * Update Time: 2019/10/20
 * Package Name:showmethe.github.core.widget.slideback
 */
class SlideBackIconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * 动画模式
     * @return
     */
    var animMode = ANIM_CLASSIC


    private var bgPath  = Path()
    private var arrowPath = Path() // 路径对象
    private var bgPaint = Paint()
    private var arrowPaint = Paint() // 画笔对象

    @ColorInt
    private var backViewColor = Color.BLACK // 控件背景色
    /**
     * 设置返回Icon的高度
     *
     */
    var backViewHeight = 0f // 控件高度
    private var arrowSize = 10f // 箭头图标大小
    private var maxSlideLength = 0f // 最大拉动距离

    private var slideLength = 0f // 当前拉动距离

    init {
        init()
    }

    /**
     * 初始化 路径与画笔
     * Path & Paint
     */
    private fun init() {
        bgPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE // 填充内部和描边
            color = backViewColor
            strokeWidth = 1f // 画笔宽度
        }
        arrowPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE // 描边
            color = Color.WHITE
            strokeWidth = 8f // 画笔宽度
            strokeJoin = Paint.Join.ROUND // * 结合处的样子 ROUND:圆弧
        }
        alpha = 0f
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 背景
        bgPath.reset() // 会多次绘制，所以先重置
        bgPath.moveTo(0f, 0f)
        bgPath.cubicTo(
            0f,
            backViewHeight * 2 / 9,
            slideLength,
            backViewHeight / 3,
            slideLength,
            backViewHeight / 2
        )
        bgPath.cubicTo(
            slideLength,
            backViewHeight * 2 / 3,
            0f,
            backViewHeight * 7 / 9,
            0f,
            backViewHeight
        )
        canvas.drawPath(bgPath, bgPaint) // 根据设置的贝塞尔曲线路径用画笔绘制
        canvas.save()

        when (animMode) {
            ANIM_CLASSIC -> drawClassAnim(canvas)
            ANIM_ROTATE -> drawRotate(canvas)
        }
        canvas.restore()
        alpha = slideLength / maxSlideLength - 0.2f // 最多0.8透明度

    }


    /**
     * 经典动画
     * @param canvas 画布
     */
    private fun drawClassAnim(canvas: Canvas) {

        // 箭头是先直线由短变长再折弯变成箭头状的
        // 依据当前拉动距离和最大拉动距离计算箭头大小值
        // 大小到一定值后开始折弯，计算箭头角度值
        val arrowZoom = slideLength / maxSlideLength // 箭头大小变化率
        val arrowAngle = if (arrowZoom < 0.75f) 0f else (arrowZoom - 0.75f) * 2 // 箭头角度变化率

        // 箭头
        arrowPath.reset() // 先重置
        // 结合箭头大小值与箭头角度值设置折线路径
        arrowPath.moveTo(
            slideLength / 2 + arrowSize * arrowAngle,
            backViewHeight / 2 - arrowZoom * arrowSize
        )
        arrowPath.lineTo(slideLength / 2 - arrowSize * arrowAngle, backViewHeight / 2)
        arrowPath.lineTo(
            slideLength / 2 + arrowSize * arrowAngle,
            backViewHeight / 2 + arrowZoom * arrowSize
        )
        canvas.drawPath(arrowPath, arrowPaint)

    }


    /**
     * 箭头旋转动画
     * @param canvas 画布
     */
    private fun drawRotate(canvas: Canvas) {
        // 三条横线旋转同时修改方向 形成箭头
        val arrowZoom = slideLength / maxSlideLength // 箭头大小变化率
        arrowPath.reset() // 先重置
        arrowPaint.strokeWidth = 8 - 4 * arrowZoom //线条宽度
        arrowPath.moveTo(slideLength / 2 - arrowSize, backViewHeight / 2 - arrowSize)
        arrowPath.lineTo(
            slideLength / 2 + arrowSize,
            backViewHeight / 2 - arrowSize + arrowSize * arrowZoom
        )  //第一条线
        arrowPaint.strokeWidth = 8f
        arrowPath.moveTo(slideLength / 2 - arrowSize - arrowSize * arrowZoom, backViewHeight / 2)
        arrowPath.lineTo(slideLength / 2 + arrowSize, backViewHeight / 2)   //第二条线
        arrowPaint.strokeWidth = 8 - 4 * arrowZoom
        arrowPath.moveTo(slideLength / 2 - arrowSize, backViewHeight / 2 + arrowSize)
        arrowPath.lineTo(
            slideLength / 2 + arrowSize,
            backViewHeight / 2 + arrowSize - arrowSize * arrowZoom
        )//第三条线
        canvas.rotate(180 * arrowZoom, slideLength / 2, backViewHeight / 2)
        canvas.drawPath(arrowPath, arrowPaint)
    }


    /**
     * 箭头回旋动画
     * @param canvas 画布
     */
    private fun drawRunningAnim(canvas: Canvas) {
        val arrowZoom = slideLength / maxSlideLength // 箭头大小变化率
        // 箭头
        arrowPath.reset() // 先重置


        canvas.drawPath(arrowPath, arrowPaint)
    }


    /**
     * 更新当前拉动距离并重绘
     *
     * @param slideLength 当前拉动距离
     */
    fun updateSlideLength(slideLength: Float) {
        // 避免无意义重绘
        if (this.slideLength == slideLength) return

        this.slideLength = slideLength// 会再次调用onDraw
        postInvalidate()
    }

    /**
     * 设置最大拉动距离
     *
     * @param maxSlideLength px值
     */
    fun setMaxSlideLength(maxSlideLength: Float) {
        this.maxSlideLength = maxSlideLength
    }

    /**
     * 设置箭头图标大小
     *
     * @param arrowSize px值
     */
    fun setArrowSize(arrowSize: Float) {
        this.arrowSize = arrowSize
    }

    /**
     * 设置返回Icon背景色
     *
     * @param backViewColor ColorInt
     */
    fun setBackViewColor(@ColorInt backViewColor: Int) {
        this.backViewColor = backViewColor
    }

    companion object {

        /**
         * 箭头动画
         */
        private val ANIM_CLASSIC = 0x0005
        private val ANIM_ROTATE = 0x0006
        private val ANIM_RUNNING = 0x0007
    }
}