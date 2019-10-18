package com.showmethe.wanandroid.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.content.res.ResourcesCompat
import com.showmethe.wanandroid.R


import java.util.ArrayList

import showmethe.github.core.util.widget.sp2px


class TextJumpAnim @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**
     * 文字大小
     */
    private var txSize = 22f
    /**
     * 画笔
     */
    private var mPaint  = Paint()
    /**
     * 字体间隙
     */
    private var gap = 12f

    /**
     * 字符串组合
     */
    private val strings = arrayOf("L", "o", "a", "d", "i", "n", "g", "。", "。", "。")
    /**
     * 变化颜色组
     */
    private val color =
        intArrayOf(-0x4fc3f7, -0xf50057, -0x6200ea, -0xe1771b, -0x71db56, -0xff8a80, -0xff4081)
    /**
     * 宽高
     */
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    /**
     * 起始点
     */
    private var content: Float = 0.toFloat()

    /**
     * 实体类集合
     */
    private val list = ArrayList<Word>()
    /**
     * 动画集合
     */
    private var set: AnimatorSet? = null
    private var animators: MutableList<Animator>? = null

    init {
        initTypeArray(attrs)
        init()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = measuredHeight
        mWidth = measuredWidth
        content = (mWidth / 4).toFloat()
        initData()
    }


    private fun initTypeArray(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TextJumpAnim)
        txSize = a.getDimension(R.styleable.TextJumpAnim_txSize, sp2px(context, 25f).toFloat())
        gap = a.getDimension(R.styleable.TextJumpAnim_gaps, 15f)
        a.recycle()
    }

    private fun init() {
        mPaint.color = Color.BLUE
        mPaint.isAntiAlias = true
        mPaint.typeface = ResourcesCompat.getFont(context,R.font.font)
        mPaint.textSize = sp2px(context, txSize).toFloat()
    }


    private fun initData() {
        for (i in strings.indices) {
            list.add(Word((mHeight / 2).toFloat(), i))
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in strings.indices) {
            mPaint.color = color[list[i].colorIndex % color.size]
            canvas.drawText(strings[i], calX(i, gap), list[i].currentY, mPaint)
        }
    }

    private fun calX(index: Int, gap: Float): Float {
        return if (index == 5) {
            content + (index - 1) * (sp2px(context, txSize) / 2f) + (index - 1) * gap
        } else content + (index - 1) * (sp2px(
            context,
            txSize
        ) / 2f) + index * gap
    }

    private fun getAnimator(word: Word, i: Int): ObjectAnimator {
        val objectAnim =
            ObjectAnimator.ofObject(word, "offset", FloatEvaluator(), 0, 0.2, 0.4, 0.6, 0.8, 1)
        objectAnim.duration = 1000
        objectAnim.startDelay = (i * 900 / 7).toLong()
        objectAnim.repeatCount = -1
        objectAnim.interpolator = AnticipateInterpolator()
        objectAnim.repeatMode = ValueAnimator.REVERSE
        if (i == 0) {
            val listener = ValueAnimator.AnimatorUpdateListener { postInvalidate() }
            objectAnim.addUpdateListener(listener)
        }
        objectAnim.addListener(Anl(word))
        return objectAnim
    }


    private fun goAnimator() {
        if (set == null) {
            set = AnimatorSet()
            animators = ArrayList()
            for (i in strings.indices) {
                val animator = getAnimator(list[i], i)
                animators!!.add(animator)
            }
            set!!.playTogether(animators)
            set!!.start()
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnim()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnim()
    }

    fun startAnim() {
        post { goAnimator() }

    }

    fun stopAnim() {
        if (set!!.isStarted) {
            set!!.cancel()
        }
    }

    private class Word(currentY: Float, var colorIndex: Int) {

        var offset: Float = 0.toFloat()
            set(offset) {
                field = offset * height
            }

        var currentY: Float = 0.toFloat()
            get() = field - this.offset

        init {
            this.currentY = currentY
        }
    }

    private class Anl(internal var word: Word) : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {

        }

        override fun onAnimationCancel(animation: Animator) {

        }

        override fun onAnimationRepeat(animation: Animator) {
            var index = word.colorIndex
            word.colorIndex = ++index
        }
    }

    companion object {
        /**
         * 垂直移动高度
         */
        private const val height = 50f
    }

}
