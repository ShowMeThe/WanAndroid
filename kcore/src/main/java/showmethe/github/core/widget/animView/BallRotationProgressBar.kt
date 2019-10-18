package showmethe.github.core.widget.animView

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator

import showmethe.github.core.R


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:54
 * Package Name:showmethe.github.core.widget.animView
 */
class BallRotationProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {


    //画笔
    private var mPaint: Paint? = null

    //球的最大半径
    private var maxRadius = DEFAULT_MAX_RADIUS.toFloat()
    //球的最小半径
    private var minRadius = DEFAULT_MIN_RADIUS.toFloat()

    //两球旋转的范围距离
    private var distance = DEFAULT_DISTANCE

    //动画的时间
    private var duration = DEFAULT_ANIMATOR_DURATION.toLong()

    private var mFirstBall: Ball? = null
    private var mSecondBall: Ball? = null

    private var mCenterX: Float = 0.toFloat()
    private var mCenterY: Float = 0.toFloat()

    private var animatorSet: AnimatorSet? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        mFirstBall = Ball()
        mSecondBall = Ball()

        val a = context.obtainStyledAttributes(attrs, R.styleable.BallRotationProgressBar)
        mFirstBall!!.color = a.getColor(R.styleable.BallRotationProgressBar_firstBallColor, DEFAULT_ONE_BALL_COLOR)
        mSecondBall!!.color = a.getColor(R.styleable.BallRotationProgressBar_secondBallColor, DEFAULT_TWO_BALL_COLOR)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)


        a.recycle()
        configAnimator()

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = (w / 2).toFloat()
        mCenterY = (h / 2).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mCenterX = (width / 2).toFloat()
        mCenterY = (height / 2).toFloat()

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val mWidth = measuredWidth / 3
        val mHeight = 150

        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight)
        } else if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize)
        } else if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight)
        }

    }

    override fun onDraw(canvas: Canvas) {
        //画两个小球，半径小的先画，半径大的后画
        if (mFirstBall!!.radius > mSecondBall!!.radius) {
            mPaint!!.color = mSecondBall!!.color
            canvas.drawCircle(mSecondBall!!.centerX, mCenterY, mSecondBall!!.radius, mPaint!!)

            mPaint!!.color = mFirstBall!!.color
            canvas.drawCircle(mFirstBall!!.centerX, mCenterY, mFirstBall!!.radius, mPaint!!)
        } else {
            mPaint!!.color = mFirstBall!!.color
            canvas.drawCircle(mFirstBall!!.centerX, mCenterY, mFirstBall!!.radius, mPaint!!)

            mPaint!!.color = mSecondBall!!.color
            canvas.drawCircle(mSecondBall!!.centerX, mCenterY, mSecondBall!!.radius, mPaint!!)
        }
    }

    /**
     * 配置属性动画
     */
    private fun configAnimator() {

        //中间半径大小
        val centerRadius = (maxRadius + minRadius) * 0.5f

        //第一个小球缩放动画，通过改变小球的半径
        //半径变化规律：中间大小->最大->中间大小->最小->中间大小
        val oneScaleAnimator = ObjectAnimator.ofFloat(mFirstBall, "radius",
                centerRadius, maxRadius, centerRadius, minRadius, centerRadius)
        //无限循环
        oneScaleAnimator.repeatCount = ValueAnimator.INFINITE


        //第一个小球位移动画，通过改变小球的圆心
        val oneCenterAnimator = ValueAnimator.ofFloat(-1f, 0f, 1f, 0f, -1f)
        oneCenterAnimator.repeatCount = ValueAnimator.INFINITE
        oneCenterAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            val x = mCenterX + distance * value
            mFirstBall!!.centerX = x
            //不停的刷新view，让view不停的重绘
            invalidate()
        }

        //第二个小球缩放动画
        //变化规律：中间大小->最小->中间大小->最大->中间大小
        val twoScaleAnimator = ObjectAnimator.ofFloat(mSecondBall, "radius", centerRadius, minRadius,
                centerRadius, maxRadius, centerRadius)
        twoScaleAnimator.repeatCount = ValueAnimator.INFINITE

        //第二个小球位移动画
        val twoCenterAnimator = ValueAnimator.ofFloat(1f, 0f, -1f, 0f, 1f)
        twoCenterAnimator.repeatCount = ValueAnimator.INFINITE
        twoCenterAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            val x = mCenterX + distance * value
            mSecondBall!!.centerX = x
        }

        //属性动画集合
        animatorSet = AnimatorSet()
        //四个属性动画一块执行
        animatorSet!!.playTogether(oneScaleAnimator, oneCenterAnimator, twoScaleAnimator, twoCenterAnimator)
        //动画一次运行时间
        animatorSet!!.duration = DEFAULT_ANIMATOR_DURATION.toLong()
        //时间插值器
        animatorSet!!.interpolator = AnticipateOvershootInterpolator()
    }

    /**
     * 小球
     */
     class Ball {
        var radius: Float = 0f//半径
        var centerX: Float = 0f//圆心
        var color: Int = 0//颜色
    }

    override fun setVisibility(v: Int) {
        if (visibility != v) {
            super.setVisibility(v)
            if (v == GONE || v == INVISIBLE) {
                stopAnimator()
            } else {
                startAnimator()
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, v: Int) {
        super.onVisibilityChanged(changedView, v)
        if (v == GONE || v == INVISIBLE) {
            stopAnimator()
        } else {
            startAnimator()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimator()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimator()
    }

    /**
     * 设置第一个球的颜色
     *
     * @param color
     */
    fun setFirstBallColor(@ColorInt color: Int) {
        mFirstBall!!.color = color
    }

    /**
     * 设置第二个球的颜色
     *
     * @param color
     */
    fun setSecondBallColor(@ColorInt color: Int) {
        mSecondBall!!.color = color
    }

    /**
     * 设置球的最大半径
     *
     * @param maxRadius
     */
    fun setMaxRadius(maxRadius: Float) {
        this.maxRadius = maxRadius
        configAnimator()
    }

    /**
     * 设置球的最小半径
     *
     * @param minRadius
     */
    fun setMinRadius(minRadius: Float) {
        this.minRadius = minRadius
        configAnimator()
    }

    /**
     * 设置两个球旋转的最大范围距离
     *
     * @param distance
     */
    fun setDistance(distance: Int) {
        this.distance = distance
    }

    fun setDuration(duration: Long) {
        this.duration = duration
        if (animatorSet != null) {
            animatorSet!!.duration = duration
        }
    }

    /**
     * 开始动画
     */
    private fun startAnimator() {
        if (visibility != VISIBLE) return

        if (animatorSet!!.isRunning) return

        if (animatorSet != null) {
            animatorSet!!.start()
        }
    }

    /**
     * 结束停止动画
     */
    fun stopAnimator() {
        if (animatorSet != null) {
            animatorSet!!.end()
        }
    }

    companion object {

        //默认小球最大半径
        private const val DEFAULT_MAX_RADIUS = 25
        //默认小球最小半径
        private const val DEFAULT_MIN_RADIUS = 3
        //默认两个小球运行轨迹直径距离
        private const val DEFAULT_DISTANCE = 40

        //默认第一个小球颜色
        private val DEFAULT_ONE_BALL_COLOR = Color.parseColor("#40df73")
        //默认第二个小球颜色
        private val DEFAULT_TWO_BALL_COLOR = Color.parseColor("#ffdf3e")

        //默认动画执行时间
        private const val DEFAULT_ANIMATOR_DURATION = 1200
    }
}
