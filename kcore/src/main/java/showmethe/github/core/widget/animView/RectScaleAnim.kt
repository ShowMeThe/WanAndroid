package showmethe.github.core.widget.animView

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


import java.util.ArrayList

import showmethe.github.core.R


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:54
 * Package Name:showmethe.github.core.widget.animView
 */
class RectScaleAnim @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr),DefaultLifecycleObserver {

    private var mPaint: Paint? = null

    private var rectChildList: MutableList<RectChild>? = null

    private var rectWidth = 60

    private var rectColor = Color.WHITE

    private var delays = intArrayOf(200, 300, 400, 100, 200, 300, 0, 100, 200)

    private var owner : LifecycleOwner? = null

    /**
     * 动画集合
     */
    var set: AnimatorSet? = null
        private set
    private var animators: MutableList<Animator>? = null

    init {
        init(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        //设置宽高
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        val specMode = View.MeasureSpec.getMode(measureSpec)
        var specSize = View.MeasureSpec.getSize(measureSpec)
        //wrap_content
        if (specMode == View.MeasureSpec.AT_MOST) {
            specSize = rectWidth * 3
        } else if (specMode == View.MeasureSpec.EXACTLY) {
        }
        return specSize
    }

    private fun measureHeight(measureSpec: Int): Int {
        val specMode = View.MeasureSpec.getMode(measureSpec)
        var specSize = View.MeasureSpec.getSize(measureSpec)
        //wrap_content
        if (specMode == View.MeasureSpec.AT_MOST) {
            specSize = rectWidth * 3
        } else if (specMode == View.MeasureSpec.EXACTLY) {
        }
        return specSize
    }

    fun init(set: AttributeSet?) {
        val a = context.obtainStyledAttributes(set, R.styleable.RectScaleAnim)
        rectWidth = a.getDimension(R.styleable.RectScaleAnim_rectWidth, 50f).toInt()
        rectColor = a.getColor(R.styleable.RectScaleAnim_rectColor, Color.WHITE)
        mPaint = Paint()
        mPaint!!.color = rectColor
        rectChildList = ArrayList()
        initList()
        a.recycle()
    }


    fun initList() {
        //line1
        for (i in 0..2) {
            rectChildList!!.add(RectChild(
                    rectWidth * i, 0,
                    rectWidth * (i + 1), rectWidth))
        }

        //line2
        for (i in 0..2) {
            rectChildList!!.add(RectChild(
                    rectWidth * i, rectWidth,
                    rectWidth * (i + 1), rectWidth * 2))
        }

        //line3
        for (i in 0..2) {
            rectChildList!!.add(RectChild(
                    rectWidth * i, rectWidth * 2,
                    rectWidth * (i + 1), rectWidth * 3))
        }

    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (child in rectChildList!!) {
            val matrix = Matrix()
            matrix.setScale(child.scale, child.scale, (child.right - rectWidth / 2).toFloat(), (child.bottom - rectWidth / 2).toFloat())
            canvas.setMatrix(matrix)
            canvas.drawRect(child.createRect(), mPaint!!)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        stopAnim()
        startAnim()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stopAnim()
        this.owner?.lifecycle?.removeObserver(this)
    }

    fun startAnim() {
        post { goAnimator() }
    }

    fun stopAnim() {
        if (set != null && set!!.isStarted) {
            set!!.cancel()
        }
    }


    fun bindLifecyle(owner: LifecycleOwner){
        this.owner = owner
        this.owner?.lifecycle?.addObserver(this)
    }

    private fun getAnimator(child: RectChild, i: Int): ObjectAnimator {
        val objectAnim = ObjectAnimator.ofObject(child, "scale", FloatEvaluator(), 0.0, 0.5, 1, 1.1)
        objectAnim.duration = 1000
        objectAnim.startDelay = delays[i].toLong()
        objectAnim.repeatCount = -1
        objectAnim.interpolator = AccelerateDecelerateInterpolator()
        objectAnim.repeatMode = ValueAnimator.REVERSE
        objectAnim.addUpdateListener(listener)
        return objectAnim
    }

    val listener = ValueAnimator.AnimatorUpdateListener { invalidate() }

    private fun goAnimator() {
        if (set == null) {
            set = AnimatorSet()
            animators = ArrayList()
            for (i in rectChildList!!.indices) {
                val animator = getAnimator(rectChildList!![i], i)
                animators!!.add(animator)
            }
            set!!.playTogether(animators)
            set!!.start()
        }
    }


    class RectChild(var left: Int, var top: Int, var right: Int, var bottom: Int) {
        var scale = 0.0f


        fun createRect(): Rect {
            return Rect(this.left, this.top, this.right, this.bottom)
        }
    }


}
