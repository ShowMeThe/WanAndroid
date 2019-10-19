package showmethe.github.core.widget.picker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Shader
import android.graphics.LinearGradient
import androidx.core.content.ContextCompat
import showmethe.github.core.R
import showmethe.github.core.util.widget.dp2px


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:51
 * Package Name:showmethe.github.core.widget.picker
 */
class WheelPicker @JvmOverloads constructor(
        context: Context, var attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr){


    /**
     * 参数
     */
    private var selectedaHeight = 120
    private var maxShowSize = 5
    private var itemCount: Int = 0
    private var scrollOffset = 0

    /**
     * 画笔
     */
    var paint: Paint? = null
    /**
     * 防止多次执行滑动
     */
    private var hasRunning = false
    /**
     * 滑动距离 里完整区域 剩余距离
     */
    private var leftHeight = 0
    /**
     * 整体宽度或高度
     */
    private var countWidth = 0.0

    private var interpolator = FastOutLinearInInterpolator()
    private var interpolator2 = FastOutLinearInInterpolator()
    /**
     * 间距
     */
    private var itemDecoration = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    /**
     * 字体颜色
     */
    private var textSelectColor = -1
    private var textUnSelectColor = -1


    /**
     * 选择指示条颜色
     */
    private var lineStart = -1
    private var lineEnd = -1

    /**
     * 阴影
     */
    private var shadow =  true
    private var shadowRadius  = 5f
    private var shadowColor  = -1

    init {
        init()
    }

    private var wheelAdapter : WheelAdapter<*>? = null

    fun setWheelAdapter(wheelAdapter : WheelAdapter<*>,defaultPos :Int = 0){
        this.wheelAdapter = wheelAdapter
        adapter = wheelAdapter
        itemCount = wheelAdapter.itemCount
        this.wheelAdapter?.textSelectColor = textSelectColor
        this.wheelAdapter?.textUnSelectColor = textUnSelectColor
        this.wheelAdapter?.shadow = shadow
        this.wheelAdapter?.shadowRadius = shadowRadius
        this.wheelAdapter?.shadowColor = shadowColor
        layoutManager = LinearLayoutManager(context,VERTICAL,false)


        smoothScrollBy(0,selectedaHeight * defaultPos,interpolator,50) //会触发滚动事件
        this.wheelAdapter?.currentPos = defaultPos + 2
        this.wheelAdapter?.notifyDataSetChanged()

    }



    fun scrollToReal(position: Int){
        if( this.wheelAdapter == null || this.wheelAdapter?.itemCount!! <=4){
            return
        }
        smoothScrollBy(0,selectedaHeight * position,interpolator,50)
        this.wheelAdapter?.currentPos = position + 2
        this.wheelAdapter?.notifyDataSetChanged()
    }


    private fun init(){
        initAttr()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        mWidth = MeasureSpec.getSize(widthSpec)
        setMaxShowSize(5)
    }

    private fun initAttr(){
        val attay = context.obtainStyledAttributes(attrs, R.styleable.WheelPicker)
        textSelectColor = attay.getResourceId(R.styleable.WheelPicker_text_selected_color,R.color.colorAccent)
        textUnSelectColor = attay.getResourceId(R.styleable.WheelPicker_text_unSelected_color,R.color.colorAccent)
        textSelectColor = ContextCompat.getColor(context,textSelectColor)
        textUnSelectColor = ContextCompat.getColor(context,textUnSelectColor)

        lineStart = attay.getColor(R.styleable.WheelPicker_line_start,Color.BLUE)
        lineEnd = attay.getColor(R.styleable.WheelPicker_line_end,Color.RED)
        shadow = attay.getBoolean(R.styleable.WheelPicker_text_shadow,true)
        shadowRadius = attay.getDimension(R.styleable.WheelPicker_text_shadow_radius,5f)
        shadowColor = attay.getColor(R.styleable.WheelPicker_text_shadow_color,ContextCompat.getColor(context,R.color.text_666666))
        attay.recycle()
    }

    private fun setMaxShowSize(maxShowSize: Int) {
        var showSize = maxShowSize
        if (showSize < 1) {
            showSize = 1
        }
        if (showSize % 2 == 0) {
            showSize += 1
        }

        this.maxShowSize = showSize
        wheelAdapter?.apply {
            setPicker( selectedaHeight, showSize)
            notifyDataSetChanged()
        }
        mHeight = showSize * selectedaHeight
        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, showSize * selectedaHeight)
        } else {
            layoutParams.height = showSize * selectedaHeight
        }
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        wheelAdapter?.apply {
            this@WheelPicker.itemCount = itemCount
        }

    }
    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        val layoutManager = layoutManager

        /**
         * 滑动停止后还能继续滚动时候，寻找临近位置自动移动到最近的position
         */
        if(itemCount> 1 && state == SCROLL_STATE_IDLE){
            val linearLayoutManager = (layoutManager as LinearLayoutManager)
            val firstPos = linearLayoutManager.findFirstVisibleItemPosition()
            val firstView = linearLayoutManager.findViewByPosition(firstPos)

            if(linearLayoutManager.orientation == VERTICAL){
                scrollOffset = firstPos * firstView!!.height - firstView.top
                leftHeight = scrollOffset % firstView.measuredHeight
                countWidth = firstView.measuredHeight * 0.5
            }else{
                scrollOffset = firstPos * firstView!!.width - firstView.left
                leftHeight = scrollOffset % firstView.measuredWidth
                countWidth = firstView.measuredWidth * 0.5
            }
            /**
             * 计算间距
             */
            if(itemDecorationCount>0 && itemCount>1 && itemDecoration == 0 ){
                itemDecoration = if(layoutManager.orientation == VERTICAL){
                    (layoutManager.findViewByPosition(firstPos+1)!!.top -
                            layoutManager.findViewByPosition(firstPos)!!.bottom )
                }else{
                    (layoutManager.findViewByPosition(firstPos+1)!!.left -
                            layoutManager.findViewByPosition(firstPos)!!.right )
                }
            }
            if(leftHeight >= countWidth){
                if(leftHeight>0 && !hasRunning){

                    if(layoutManager.orientation == VERTICAL){
                        smoothScrollBy(0,linearLayoutManager.findViewByPosition(firstPos + 1)!!.top
                                - itemDecoration,interpolator)
                    }else{
                        smoothScrollBy(linearLayoutManager.findViewByPosition(firstPos + 1)!!.left
                                - itemDecoration,0,interpolator)
                    }

                    wheelAdapter?.currentPos = firstPos + 1 + 2
                    wheelAdapter?.notifyDataSetChanged()
                    onItemTextChange?.invoke(firstPos + 1)
                    hasRunning = true
                }
            }else{
                if(layoutManager.orientation == VERTICAL){
                    smoothScrollBy(0,linearLayoutManager.findViewByPosition(firstPos)!!.top -
                            itemDecoration - (firstView.layoutParams as LayoutParams).topMargin,interpolator2)
                }else{
                    smoothScrollBy(linearLayoutManager.findViewByPosition(firstPos)!!.left
                            - itemDecoration  - (firstView.layoutParams as LayoutParams).leftMargin,0,interpolator2)
                }

                wheelAdapter?.currentPos = firstPos + 2
                wheelAdapter?.notifyDataSetChanged()
                onItemTextChange?.invoke(firstPos)
                hasRunning = true
            }
        }else{
            hasRunning = false
        }

    }


    var onItemTextChange : ((position:Int)->Unit)? = null

    fun  setOnItemTextChangeListener(onItemTextChange : ((position:Int)->Unit)){
        this.onItemTextChange = onItemTextChange
    }


    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val top = Rect(0, 0,
                mWidth, mHeight / 2 - selectedaHeight / 2)
        val center = Rect(0, top.bottom,
                mWidth, mHeight / 2 + selectedaHeight / 2)

        if (paint == null) {
            paint = Paint()
            paint?.isAntiAlias = true
            paint?.strokeWidth = dp2px(context, 0.3f)
            paint?.style = Paint.Style.STROKE
            val lg = LinearGradient(0f, 0f, 100f, 100f, lineStart, lineEnd, Shader.TileMode.MIRROR)
            paint?.shader = lg
        }
        canvas.drawLine(center.right.toFloat()/3,center.top.toFloat(),center.right.toFloat()*(2f/3f),center.top.toFloat(),paint!!)
        canvas.drawLine(center.right.toFloat()/3,center.bottom.toFloat(),center.right.toFloat()*(2f/3f),center.bottom.toFloat(),paint!!)
    }


}