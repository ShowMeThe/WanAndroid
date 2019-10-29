package showmethe.github.core.widget.common

import android.content.Context
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.ImageView

import android.graphics.*
import showmethe.github.core.R
import showmethe.github.core.photoview.PhotoView

/**
 * Author: showMeThe
 * Update Time: 2019/10/28
 * Package Name:showmethe.github.core.widget.common
 */
class ProgressImageView @JvmOverloads constructor(
        context: Context, var attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PhotoView(context, attrs, defStyleAttr) {

    private val proPaint = Paint()
    private val bgPaint = Paint()
    private val circlePaint = Paint()
    private var  progressPercent = 0f
    private var  radius  = 0f
    private val rect = RectF()
    private var  centerX = 0f
    private var centerY = 0f

    init {
        proPaint.style = Paint.Style.FILL_AND_STROKE
        proPaint.isAntiAlias = true
        proPaint.color = Color.WHITE
        proPaint.alpha = (255 * 0.6f).toInt()

        //设置抗锯齿
        bgPaint.isAntiAlias = true
        bgPaint.color = Color.BLACK

        circlePaint.isAntiAlias = true
        circlePaint.strokeWidth = 0.5f
        circlePaint.color = Color.WHITE
        circlePaint.style = Paint.Style.STROKE
        circlePaint.alpha = (255 * 0.8f).toInt()

        initAttr()
    }


    private fun initAttr(){
        val array = context.obtainStyledAttributes(attrs, R.styleable.ProgressImageView)
        val zoomable = array.getBoolean(R.styleable.ProgressImageView_zoomable,false)
        array.recycle()

        isZoomable = zoomable
    }


    override fun draw(canvas: Canvas) {
        canvas.drawRect(0f,0f,measuredWidth.toFloat(),measuredHeight.toFloat(),bgPaint)
        if(progressPercent <=99){
            if(radius == 0f){
                centerX = measuredWidth/2f
                centerY = measuredHeight/2f
                radius = measuredWidth.div(24f)
                rect.set(centerX - radius,centerY - radius,centerX + radius,centerY + radius)
            }
            canvas.drawArc(rect,270f,(progressPercent/100) * 360f,true,proPaint)
            canvas.drawCircle(centerX,centerY,radius,circlePaint)
        }
        super.draw(canvas)
    }


    fun setPercentage(percentage: Float) {
        this.progressPercent = percentage
        invalidate()
    }

}