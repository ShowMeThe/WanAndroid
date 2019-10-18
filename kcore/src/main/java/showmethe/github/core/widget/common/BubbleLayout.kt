package showmethe.github.core.widget.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

import showmethe.github.core.R
import showmethe.github.core.util.widget.dip2px


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:51
 * Package Name:showmethe.github.core.widget.common
 */
/**
 * 带箭头的气泡布局
 */
class BubbleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var Direction = BOTTOM

    private var radius: Int = 0

    private var basePoint: Point? = null

    private var offset: Int = 0

    private var borderPaint: Paint? = null

    private var path: Path? = null

    private var rectf: RectF? = null

    private var shadowWidth: Int = 0

    private var shadowColor: Int = 0

    private var background: Int = 0

    private var distance: Float = 0.toFloat()

    private val backgroundDrawable: Int = 0

    private var defaultPadding: Float = 0.toFloat()

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    internal var far: Float = 0.toFloat()

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        initAttr(context, attrs)
        borderPaint = Paint()
        borderPaint!!.isAntiAlias = true
        borderPaint!!.color = background
        borderPaint!!.setShadowLayer(shadowWidth.toFloat(), 0f, 0f, shadowColor)



        path = Path()
        rectf = RectF()
        basePoint = Point()


        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }


    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.BubbleLayout)
        background = array.getColor(R.styleable.BubbleLayout_backgroundColor, Color.WHITE)
        shadowColor = array.getColor(R.styleable.BubbleLayout_shadowColor, Color.parseColor("#999999"))
        shadowWidth = array.getDimension(R.styleable.BubbleLayout_shadow, dip2px(context, 4f).toFloat()).toInt()
        val defaultValue = dip2px(context, 10f)
        radius = array.getDimension(R.styleable.BubbleLayout_radius, defaultValue.toFloat()).toInt()
        distance = array.getDimension(R.styleable.BubbleLayout_distance, defaultValue.toFloat())
        defaultPadding = array.getDimension(R.styleable.BubbleLayout_aroundPadding, defaultValue.toFloat())
        offset = array.getDimension(R.styleable.BubbleLayout_offset, 0f).toInt()
        Direction = array.getInt(R.styleable.BubbleLayout_directions, BOTTOM)
        array.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //控制三角形形状合理范围正常显示
        distance = if (distance > defaultPadding) defaultPadding else distance

        if (basePoint!!.x > 0 && basePoint!!.y > 0) {
            when (Direction) {
                LEFT -> drawLeftTriangle(canvas)
                TOP -> drawTopTriangle(canvas)
                RIGHT -> drawRightTriangle(canvas)
                BOTTOM -> drawBottomTriangle(canvas)
            }
        }
    }


    private fun drawLeftTriangle(canvas: Canvas) {
        val triangularLength = distance
        if (triangularLength == 0f) {
            return
        }

        path!!.addRoundRect(rectf, radius.toFloat(), radius.toFloat(), Path.Direction.CCW)
        path!!.moveTo(basePoint!!.x.toFloat(), basePoint!!.y - triangularLength / 2)
        path!!.lineTo(basePoint!!.x - triangularLength / 2, basePoint!!.y.toFloat())
        path!!.lineTo(basePoint!!.x.toFloat(), basePoint!!.y + triangularLength / 2)
        path!!.close()
        canvas.drawPath(path!!, borderPaint!!)
    }

    private fun drawTopTriangle(canvas: Canvas) {
        val triangularLength = distance
        if (triangularLength == 0f) {
            return
        }

        path!!.addRoundRect(rectf, radius.toFloat(), radius.toFloat(), Path.Direction.CCW)
        path!!.moveTo(basePoint!!.x + triangularLength / 2, basePoint!!.y.toFloat())
        path!!.lineTo(basePoint!!.x.toFloat(), basePoint!!.y - triangularLength / 2)
        path!!.lineTo(basePoint!!.x - triangularLength / 2, basePoint!!.y.toFloat())
        path!!.close()
        canvas.drawPath(path!!, borderPaint!!)
    }

    private fun drawRightTriangle(canvas: Canvas) {
        val triangularLength = distance
        if (triangularLength == 0f) {
            return
        }

        path!!.addRoundRect(rectf, radius.toFloat(), radius.toFloat(), Path.Direction.CCW)
        path!!.moveTo(basePoint!!.x.toFloat(), basePoint!!.y - triangularLength / 2)
        path!!.lineTo(basePoint!!.x + triangularLength / 2, basePoint!!.y.toFloat())
        path!!.lineTo(basePoint!!.x.toFloat(), basePoint!!.y + triangularLength / 2)
        path!!.close()
        canvas.drawPath(path!!, borderPaint!!)
    }

    private fun drawBottomTriangle(canvas: Canvas) {
        val triangularLength = distance
        if (triangularLength == 0f) {
            return
        }

        path!!.addRoundRect(rectf, radius.toFloat(), radius.toFloat(), Path.Direction.CCW)
        path!!.moveTo(basePoint!!.x + triangularLength, basePoint!!.y.toFloat())
        path!!.lineTo(basePoint!!.x.toFloat(), basePoint!!.y + triangularLength)
        path!!.lineTo(basePoint!!.x - triangularLength, basePoint!!.y.toFloat())
        path!!.close()
        canvas.drawPath(path!!, borderPaint!!)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = measuredWidth
        mHeight = measuredHeight

        setPadding(defaultPadding.toInt(), defaultPadding.toInt(), defaultPadding.toInt(), defaultPadding.toInt())

        rectf!!.left = defaultPadding
        rectf!!.top = defaultPadding
        rectf!!.right = w - defaultPadding
        rectf!!.bottom = h - defaultPadding

        when (Direction) {
            LEFT -> {
                basePoint!!.x = defaultPadding.toInt()
                basePoint!!.y = h / 2
            }
            TOP -> {
                basePoint!!.x = w / 2
                basePoint!!.y = defaultPadding.toInt()
            }
            RIGHT -> {
                basePoint!!.x = w - defaultPadding.toInt()
                basePoint!!.y = h / 2
            }
            BOTTOM -> {
                basePoint!!.x = w / 2
                basePoint!!.y = h - defaultPadding.toInt()
            }
        }

        if (offset != 0) {
            addOffset()
        }
    }

    private fun addOffset() {
        when (Direction) {
            LEFT, RIGHT -> {
                far = (((mHeight - distance) / 2).toDouble() - defaultPadding.toDouble() - radius * 1.2).toFloat()
                if (offset > 0) {
                    if (basePoint!!.y + offset >= mHeight.toDouble() - defaultPadding.toDouble() - radius * 1.2) {
                        basePoint!!.y += far.toInt()
                    } else {
                        basePoint!!.y += offset
                    }
                } else if (offset < 0) {
                    if (basePoint!!.y + offset <= defaultPadding + radius * 1.2) {
                        basePoint!!.y -= far.toInt()
                    } else {
                        basePoint!!.y += offset
                    }
                }
            }
            TOP, BOTTOM -> {
                far = (((mWidth - distance) / 2).toDouble() - defaultPadding.toDouble() - radius * 1.2).toFloat()
                if (offset > 0) {
                    if (basePoint!!.x + offset >= mWidth.toDouble() - defaultPadding.toDouble() - radius * 1.2) {
                        basePoint!!.x += far.toInt()
                    } else {
                        basePoint!!.x += offset
                    }
                } else if (offset < 0) {
                    if (basePoint!!.x + offset <= defaultPadding + radius * 1.2) {
                        basePoint!!.x -= far.toInt()
                    } else {
                        basePoint!!.x += offset
                    }
                }
            }
        }

    }

    fun setOffset(offset: Int) {
        this.offset = offset
        addOffset()
        invalidate()
    }

    companion object {


        val LEFT = 1
        val TOP = 2
        val RIGHT = 3
        val BOTTOM = 4
    }

}
