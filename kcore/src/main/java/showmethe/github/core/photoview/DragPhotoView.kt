package showmethe.github.core.photoview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 10:22
 * Package Name:showmethe.github.core.photoview
 */
class DragPhotoView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : PhotoView(context, attr, defStyle) {

    private val mPaint: Paint = Paint()

    // downX
    private var mDownX = 0f
    // down Y
    private var mDownY = 0f

    private var centerX  = 0f
    private var centerY  = 0f

    private var mTranslateY  = 0f
    private var mTranslateX  = 0f
    private var mScale = 1f
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var minScale = 0.5f
    private var mAlpha = 255
    private var canFinish = false
    private var isAnimate = false

    //is event on PhotoView
    private var isTouchEvent = false


    private val alphaAnimation: ValueAnimator
        get() {
            val animator = ValueAnimator.ofInt(mAlpha, 255)
            animator.duration = DURATION
            animator.addUpdateListener { valueAnimator ->
                mAlpha = valueAnimator.animatedValue as Int
            }

            return animator
        }

    private val translateYAnimation: ValueAnimator
        get() {
            val animator = ValueAnimator.ofFloat(mTranslateY, 0f)
            animator.duration = DURATION
            animator.addUpdateListener { valueAnimator ->
                mTranslateY = valueAnimator.animatedValue as Float
            }

            return animator
        }

    private val translateXAnimation: ValueAnimator
        get() {
            val animator = ValueAnimator.ofFloat(mTranslateX, 0f)
            animator.duration = DURATION
            animator.addUpdateListener { valueAnimator ->
                mTranslateX = valueAnimator.animatedValue as Float
            }

            return animator
        }

    private val scaleAnimation: ValueAnimator
        get() {
            val animator = ValueAnimator.ofFloat(mScale, 1f)
            animator.duration = DURATION
            animator.addUpdateListener { valueAnimator ->
                mScale = valueAnimator.animatedValue as Float
                invalidate()
            }

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                    isAnimate = true
                }

                override fun onAnimationEnd(animator: Animator) {
                    isAnimate = false
                    animator.removeAllListeners()
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            })
            return animator
        }

    init {
        mPaint.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        mPaint.alpha = mAlpha
        alphaListener?.invoke(mAlpha.toFloat())
        canvas.translate(mTranslateX, mTranslateY)
        canvas.scale(mScale, mScale, centerX, centerY)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mWidth = w
        mHeight = h

        centerX = (mWidth / 2).toFloat()
        centerY = (mHeight / 2).toFloat()

        MAX_TRANSLATE_Y = (mHeight * 0.8).toInt()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        //only scale == 1 can drag
        if (scale == 1f) {

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onActionDown(event)

                    //change the canFinish flag
                    canFinish = !canFinish
                }
                MotionEvent.ACTION_MOVE -> {

                    //in viewpager
                    if (mTranslateY == 0f && mTranslateX != 0f) {
                        //如果不消费事件，则不作操作
                        if (!isTouchEvent) {
                            mScale = 1f
                            return super.dispatchTouchEvent(event)
                        }
                    }

                    //single finger drag  down
                    if (mTranslateY >= 0 && event.pointerCount == 1) {
                        onActionMove(event)
                        //如果有上下位移 则不交给viewpager
                        if (mTranslateY != 0f) {
                            isTouchEvent = true
                        }
                        return true
                    }

                    //防止下拉的时候双手缩放
                    if (mTranslateY >= 0 && mScale < 0.95) {
                        return true
                    }
                }

                MotionEvent.ACTION_UP ->
                    //防止下拉的时候双手缩放
                    if (event.pointerCount == 1) {
                        onActionUp()
                        isTouchEvent = false
                        //judge finish or not
                        postDelayed({
                            if (mTranslateX == 0f && mTranslateY == 0f && canFinish) {
                                tabListener?.invoke(this@DragPhotoView)
                            }
                            canFinish = false
                        }, 300)
                    }
            }
        }

        return super.dispatchTouchEvent(event)
    }

    private fun onActionUp() {
        performAnimation()
        if (mTranslateY > MAX_TRANSLATE_Y_FINISH) {
            listener?.invoke(
                this,
                mTranslateX,
                mTranslateY,
                mWidth.toFloat(),
                mHeight.toFloat()
            )
        }
    }

    private fun onActionMove(event: MotionEvent) {
        val moveY = event.y
        val moveX = event.x
        mTranslateX = moveX - mDownX
        mTranslateY = moveY - mDownY

        //保证上划到到顶还可以继续滑动
        if (mTranslateY < 0) {
            mTranslateY = 0f
        }

        val percent = mTranslateY / MAX_TRANSLATE_Y
        if (mScale in minScale..1f) {
            mScale = 1 - percent

            mAlpha = (255 * (1 - percent)).toInt()
            if (mAlpha > 255) {
                mAlpha = 255
            } else if (mAlpha < 0) {
                mAlpha = 0
            }
        }
        if (mScale < minScale) {
            mScale = minScale
        } else if (mScale > 1f) {
            mScale = 1f
        }

        invalidate()
    }

    private fun performAnimation() {
        scaleAnimation.start()
        translateXAnimation.start()
        translateYAnimation.start()
        alphaAnimation.start()
    }

    private fun onActionDown(event: MotionEvent) {
        mDownX = event.x
        mDownY = event.y
    }



    private var listener: ((view: DragPhotoView, translateX: Float, translateY: Float, w: Float, h: Float)->Unit)? = null
    fun setOnExitListener(listener: (view: DragPhotoView, translateX: Float, translateY: Float, w: Float, h: Float)->Unit) {
        this.listener = listener
    }

    private var alphaListener : ((alpha:Float)->Unit)? = null
    fun setOnAlphaListener(alphaListener: (alpha:Float)->Unit) {
        this.alphaListener = alphaListener
    }

    private var tabListener : ((view:DragPhotoView)->Unit )? = null
    fun setOnTabListener(tabListener : ((view:DragPhotoView)->Unit )){
        this.tabListener  = tabListener
    }

    fun finishAnimationCallBack() {
        mTranslateX = -mWidth / 2 + mWidth * mScale / 2
        mTranslateY = -mHeight / 2 + mHeight * mScale / 2
        invalidate()
    }

    companion object {
        private var MAX_TRANSLATE_Y = 500
        private const val MAX_TRANSLATE_Y_FINISH = 500
        private const val DURATION: Long = 300
    }
}
