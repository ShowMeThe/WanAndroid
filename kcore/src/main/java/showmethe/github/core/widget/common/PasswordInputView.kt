package showmethe.github.core.widget.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

import android.text.InputFilter
import android.text.InputType
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.View
import android.widget.EditText

import java.util.ArrayList

import showmethe.github.core.R


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:51
 * Package Name:showmethe.github.core.widget.common
 */
@SuppressLint("AppCompatCustomView")
class PasswordInputView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : EditText(mContext, attrs, defStyleAttr), View.OnFocusChangeListener {

    /**
     * 间隔
     */
    private val PWD_SPACING = 25
    /**
     * 密码大小
     */
    private val PWD_SIZE = 15
    /**
     * 密码长度
     */
    private var PWD_LENGTH = 3
    /**
     * 宽度
     */
    private var mWidth: Int = 0
    /**
     * 高度
     */
    private var mHeight: Int = 0
    /**
     * 密码框
     */
    private val mRect: Rect? = null

    /**
     * 密码画笔
     */
    private val mPwdPaint = Paint()

    /**
     * 密码框画笔
     */
    private val mRectPaint: Paint
    /**
     * 输入的密码长度
     */
    private var mInputLength: Int = 0

    /**
     * 输入结束监听
     */
    private var mOnInputFinishListener: OnInputFinishListener? = null

    /**
     * 背景画笔
     *
     */
    private val paint = Paint()

    /**
     * 拿到焦点
     */
    private var foucus: Boolean = false
    /**
     * 输入限制
     */
    private var limit: Int = 0

    /**
     * 密码框缓冲
     * @param context
     */
    private val rects = ArrayList<Rect>()

    init {
        // 初始化密码画笔
        mPwdPaint.color = Color.BLACK
        mPwdPaint.style = Paint.Style.FILL
        mPwdPaint.isAntiAlias = true
        // 初始化密码框
        mRectPaint = Paint()
        mRectPaint.style = Paint.Style.STROKE
        mRectPaint.color = Color.LTGRAY
        mRectPaint.isAntiAlias = true

        paint.color = Color.WHITE

        initType(mContext, attrs)
        for (i in 0 until PWD_LENGTH) {
            rects.add(Rect())
        }
    }


    private fun initType(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PasswordInputView)
        PWD_LENGTH = array.getInteger(R.styleable.PasswordInputView_passwordLength, 6)
        limit = array.getInt(R.styleable.PasswordInputView_passwordInputType, 0)

        PWD_LENGTH = if (PWD_LENGTH > 0) PWD_LENGTH else 6
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(PWD_LENGTH))
        if (limit == 0) {
            inputType = InputType.TYPE_CLASS_NUMBER
        } else {
            inputType = InputType.TYPE_CLASS_TEXT
        }
        array.recycle()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
        isFocusable = true
        isFocusableInTouchMode = true
        isCursorVisible = false
        setTextIsSelectable(false)
        textSize = mHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //画背景

        //canvas.drawRect(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), paint)

        // 计算每个密码框宽度
        val rectWidth = (mWidth - PWD_SPACING * (PWD_LENGTH - 1)) / PWD_LENGTH
        // 绘制密码框
        for (i in 0 until PWD_LENGTH) {
            val rect = rects[i]
            val left = (rectWidth + PWD_SPACING) * i
            val top = 2
            val right = left + rectWidth
            val bottom = mHeight - top
            rect.set(left, top, right, bottom)
            canvas.drawRect(rect, mRectPaint)
        }

        // 绘制密码
        for (i in 0 until mInputLength) {
            val cx = rectWidth / 2 + (rectWidth + PWD_SPACING) * i
            val cy = mHeight / 2
            canvas.drawCircle(cx.toFloat(), cy.toFloat(), PWD_SIZE.toFloat(), mPwdPaint)
        }
    }

    override fun getDefaultMovementMethod(): MovementMethod? {
        //关闭 copy/paste/cut 长按文字菜单，使文字不可长按选中
        return null
    }

    override fun onTextChanged(text: CharSequence, start: Int,
                               lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (start != text.length) {
            setSelection(getText().toString().length)
        }
        this.mInputLength = text.toString().length
        invalidate()
        if (mInputLength == PWD_LENGTH && mOnInputFinishListener != null) {
            mOnInputFinishListener!!.onInputFinish(text.toString())
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        foucus = hasFocus
        if (foucus) {
            setSelection(text.toString().length)
        }
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (selEnd != text.length) {
            setSelection(text.toString().length)
        }
    }

    interface OnInputFinishListener {
        /**
         * 密码输入结束监听
         *
         * @param password
         */
        fun onInputFinish(password: String)
    }

    /**
     * 设置输入完成监听
     *
     * @param onInputFinishListener
     */
    fun setOnInputFinishListener(
            onInputFinishListener: OnInputFinishListener) {
        this.mOnInputFinishListener = onInputFinishListener
    }

}
