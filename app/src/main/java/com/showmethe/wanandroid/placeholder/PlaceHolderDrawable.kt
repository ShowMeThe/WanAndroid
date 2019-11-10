package com.showmethe.wanandroid.placeholder

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import java.lang.Exception
import java.lang.ref.WeakReference

/**
 *  com.example.ken.kmvvm.placeholder
 *  2019/11/10
 *  19:43
 */
class PlaceHolderDrawable(view: View) : ShapeDrawable() {

    private val defaultColor = Color.parseColor("#eeeeee")
    private val heightColor = Color.parseColor("#f7f7f7")

    private val mColors: IntArray = intArrayOf(defaultColor,heightColor,defaultColor)
    private var valueAnimator: ValueAnimator? = null
    private val duration = 2000L
    private val interpolator = LinearInterpolator()
    private var xStartCoordinate = 0f
    private var xEndCoordinate = 0f
    private var animatedValue = 0
    private var mCanvasWidth = 0
    private var mCanvasHeight = 0

    private var mGradientCanvas: Canvas? = null
    private var mGradientLayer: Bitmap? = null

    private var mBackgroundCanvas: Canvas? = null
    private var mBackgroundLayer: Bitmap? = null
    private var target: WeakReference<View> = WeakReference(view)

    init {

        target.get()?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                cancelAnimation()
            }
        })

        shape = getReboundRect(8f)

    }

    private fun getReboundRect(radius: Float): RoundRectShape {
        return RoundRectShape(floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius), null, null)
    }

    override fun draw(canvas: Canvas) {
        if (bounds.width() <= 0
                || bounds.height() <= 0) {
            super.draw(canvas)
            return
        }
        if (target.get() == null) {
            cancelAnimation()
            return
        }


        if (mGradientCanvas == null) {

            mCanvasWidth = bounds.width()
            mCanvasHeight = bounds.height()
            mGradientLayer = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ALPHA_8)
            mGradientCanvas = Canvas(mGradientLayer!!)

            mBackgroundLayer = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888)
            mBackgroundCanvas = Canvas(mBackgroundLayer!!)
        }

        if (mBackgroundLayer!!.isRecycled
            || mGradientLayer!!.isRecycled ) {
            super.draw(canvas)
            return
        }

        paint.color = defaultColor
        shape.draw(mBackgroundCanvas, paint)

        canvas.drawBitmap(mBackgroundLayer!!, 0f, 0f, paint)

        xStartCoordinate = animatedValue.toFloat()
        xEndCoordinate = xStartCoordinate + mCanvasWidth/2
        val floatArray = floatArrayOf( 0f,0.4f,0.8f)

        paint.shader = LinearGradient(xStartCoordinate, 0f, xEndCoordinate, 0f, mColors, floatArray, Shader.TileMode.CLAMP)
        shape.draw(mGradientCanvas, paint)
        canvas.drawBitmap(mGradientLayer!!, 0f, 0f, paint)

    }


    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        mCanvasWidth = bounds.width()
        mCanvasHeight = bounds.height()
    }


    fun setupAnimator() {
        xStartCoordinate = 0f
        valueAnimator = ValueAnimator.ofInt(0, target.get()!!.measuredWidth)
        valueAnimator?.duration = duration
        valueAnimator?.interpolator = interpolator
        valueAnimator?.repeatMode = ValueAnimator.RESTART
        valueAnimator?.repeatCount = ValueAnimator.INFINITE
        valueAnimator?.addUpdateListener { animation ->
            animatedValue = animation.animatedValue as Int
            invalidateSelf()
        }
        valueAnimator?.start()
    }


    fun cancelAnimation() {

        if (valueAnimator != null) {
            valueAnimator?.cancel()
            valueAnimator = null
        }

        if (mGradientLayer != null) {
            if (!mGradientLayer?.isRecycled!!) {
                mGradientLayer?.recycle()
            }
            mGradientLayer = null
        }
        if (mBackgroundLayer != null) {
            if (!mBackgroundLayer?.isRecycled!!) {
                mBackgroundLayer?.recycle()
            }

            mGradientLayer = null
        }
    }

}
