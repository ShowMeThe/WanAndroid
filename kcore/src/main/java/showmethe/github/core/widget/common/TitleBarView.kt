package showmethe.github.core.widget.common

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.MarginLayoutParamsCompat
import showmethe.github.core.R
import showmethe.github.core.util.widget.dp2px
import showmethe.github.core.util.widget.px2sp

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:53
 * Package Name:showmethe.github.core.widget.common
 */
class TitleBarView @JvmOverloads constructor(
        context: Context, var attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {


    private var navButton :  ImageView? = null
    private var navIconDrawable : Drawable? = null
    private var navIconTintList : ColorStateList? = null
    private var navIconSize = 10f
    private var navDefaultMargin = 15f
    private var navHasMeasure = false

    private  var navTextView : TextView? = null
    var navContent :String? = null
     set(value) {
         field =value
         navTextView?.text = value
     }
    private var navContentMargin = 15f
    private var navContentSize = -1f
    private var navContentColor : ColorStateList? = null
    private var contentHasMeasure = false


    private var titleTextView :TextView? = null
    var title :String? = null
      set(value) {
          field = value
          titleTextView?.text = value
      }
    private var titleSize = -1f
    private var titleColor : ColorStateList? = null
    private var titleStyle = 0


    private var menuButton : Button? = null
    private var menuDrawable:Drawable? = null
    private var menuDrawablePadding = 15f
    private var menuButtonMargin = 15f
    private var menuIcon :Drawable? = null
    private var menuIconTint:ColorStateList? = null
    private var menuContent :String? = null
    private var menuContentColor :ColorStateList? = null
    private var menuContentSize = -1f
    private var menuContentStyle = 0

    init {
        init()
    }

    fun init(){
        initAttr()
        initView()
    }



    private fun initAttr() {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView)
        navIconDrawable = array.getDrawable(R.styleable.TitleBarView_navigationIcon)
        navIconTintList = array.getColorStateList(R.styleable.TitleBarView_navigationTint)
        navIconSize = array.getDimension(R.styleable.TitleBarView_navigationIconSize,10f)
        navDefaultMargin = array.getDimension(R.styleable.TitleBarView_navMarginStart,navDefaultMargin)

        navContent = array.getString(R.styleable.TitleBarView_navContent)
        navContentMargin = array.getDimension(R.styleable.TitleBarView_navContentMarginStart,navContentMargin)
        navContentSize = array.getDimension(R.styleable.TitleBarView_navContentTextSize,-1f)
        navContentColor = array.getColorStateList(R.styleable.TitleBarView_navContentTextColor)

        title =  array.getString(R.styleable.TitleBarView_title)
        titleSize = array.getDimension(R.styleable.TitleBarView_titleTxSize,-1f)
        titleColor = array.getColorStateList(R.styleable.TitleBarView_titleTxColor)
        titleStyle = array.getInt(R.styleable.TitleBarView_titleStyle,0)


        menuContent =  array.getString(R.styleable.TitleBarView_menuContent)
        menuDrawable = array.getDrawable(R.styleable.TitleBarView_menuIcon)
        menuDrawablePadding =  array.getDimension(R.styleable.TitleBarView_menuIconPadding,menuDrawablePadding)
        menuButtonMargin = array.getDimension(R.styleable.TitleBarView_menuMarginEnd,menuButtonMargin)
        menuIconTint = array.getColorStateList(R.styleable.TitleBarView_menuIconTint)
        menuContentColor = array.getColorStateList(R.styleable.TitleBarView_menuContentColor)
        menuContentSize = array.getDimension(R.styleable.TitleBarView_menuContentSize,-1f)
        menuContentStyle = array.getInt(R.styleable.TitleBarView_menuContentStyle,0)
        array.recycle()
    }


    private fun initView() {

        navIconDrawable?.apply {
            setNavIcon(this)
        }
        setNavTxContent(navContent)

        navIconTintList?.apply {
            setNavigationTintList(this)
        }

        setTitleView(title)

        setMenuButton(menuContent)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var navRight = 0
        if(shouldLayout(navButton) && !navHasMeasure){
            navRight = navButton!!.measuredWidth + getHorizontalMargins(navButton!!)
            navHasMeasure = true
        }
        if(shouldLayout(navTextView) && !contentHasMeasure){
            val lp = navTextView!!.layoutParams as LayoutParams
            lp.marginStart =  navContentMargin.toInt() + navRight
            navTextView!!.layoutParams = lp
            contentHasMeasure = true
        }
    }

    private fun setMenuButton(content: String?){
        if(menuButton == null){
            menuButton = AppCompatButton(context)
            menuButton?.setBackgroundColor(Color.TRANSPARENT)
            menuContentColor?.apply {
                setMenuContentColor(this)
            }
            menuDrawable?.apply {
                menuDrawable?.setBounds(0,0,this.intrinsicWidth,this.intrinsicHeight)
                menuButton?.compoundDrawableTintList = menuIconTint
                menuButton?.setCompoundDrawables(null,null,this,null)
                menuButton?.compoundDrawablePadding = menuDrawablePadding.toInt()
            }
            menuButton?.setSingleLine()
            menuButton?.ellipsize = TextUtils.TruncateAt.END
            if(menuContentSize!=-1f){
                menuButton?.textSize = px2sp(context,menuContentSize).toFloat()
            }
            val lp = generateDefaultLayoutParams()
            lp.addRule(CENTER_VERTICAL)
            lp.addRule(ALIGN_PARENT_END)
            lp.marginEnd = menuButtonMargin.toInt()
            menuButton?.layoutParams = lp


            when(menuContentStyle){
                0 -> menuButton?.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
                1 ->menuButton?.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                2 ->menuButton?.setTypeface(Typeface.DEFAULT, Typeface.ITALIC)
                3 ->{
                    menuButton?. paint!!.flags = ( Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
                }
                4 ->{
                    menuButton?. paint!!.flags = ( Paint.UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
                }
            }
            addView(menuButton)

            menuButton?.setOnClickListener {
                onMenuClick?.invoke(it)
            }
        }
        if(content.isNullOrEmpty()){
            menuButton?.text = ""
        }else{
            menuButton?.text = content
        }

    }

    private var onMenuClick:((view:View) ->Unit)? = null
    fun setOnMenuClickListener(onMenuClick:((view:View) ->Unit)){
        this.onMenuClick = onMenuClick
    }

    private var onNavigationClick:((view:View) ->Unit)? = null
    fun setOnNavigationClickListener(onNavigationClick:((view:View) ->Unit)){
        this.onNavigationClick = onNavigationClick
    }

    fun setMenuIconColor(color: ColorStateList){
        menuButton?.compoundDrawableTintList = color
    }


    fun setMenuIcon(drawable: Drawable){
        drawable.setBounds(0,0,drawable.intrinsicWidth,drawable.intrinsicHeight)
        menuButton?.setCompoundDrawables(null,null,drawable,null)
    }

    fun setMenuIconColor(color: Int){
        menuButton?.compoundDrawableTintList = ColorStateList.valueOf(color)
    }

    fun setMenuContentColor(color: Int){
        menuButton?.setTextColor(color)
    }


    fun setMenuContentColor(color: ColorStateList){
        menuButton?.setTextColor(color)
    }


    fun setMenuContent(content: String){
        this.menuContent = content
        setMenuButton(content)
    }

    fun getMenuContent() = menuContent


    private fun  setTitleView(content: String?){
        if(titleTextView == null){
            titleTextView = AppCompatTextView(context)
            titleColor?.apply {
                setTitleColor(this)
            }
            titleTextView?.setSingleLine()
            titleTextView?.ellipsize = TextUtils.TruncateAt.END
            if(titleSize!=-1f){
                titleTextView?.textSize = px2sp(context,titleSize).toFloat()
            }
            val lp = generateDefaultLayoutParams()
            lp.addRule(CENTER_IN_PARENT)
            titleTextView?.layoutParams = lp
            addView(titleTextView)
            when(titleStyle){
                1 ->titleTextView?.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                2 ->titleTextView?.setTypeface(Typeface.DEFAULT, Typeface.ITALIC)
                3 ->{
                    titleTextView?. paint!!.flags = ( Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
                }
                4 ->{
                    titleTextView?. paint!!.flags = ( Paint.UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
                }
            }
        }

        if(content.isNullOrEmpty()){
            titleTextView?.text = ""
        }else{
            titleTextView?.text = content
        }
    }


    fun setTitleStyle(style: Int){
        titleTextView?.setTypeface(Typeface.DEFAULT,style)
    }

    fun setTitleColor(color: Int){
        titleTextView?.setTextColor(color)
    }

    fun setTitleColor(color: ColorStateList){
        titleTextView?.setTextColor(color)
    }


    private fun setNavTxContent(content:String?){
        if(navTextView == null){
            navTextView = AppCompatTextView(context)
            navContentColor?.apply {
                setNavigationTextColor(this)
            }
            navTextView?.setSingleLine()
            navTextView?.ellipsize = TextUtils.TruncateAt.END
            if(navContentSize!=-1f){
                navTextView?.textSize = px2sp(context,navContentSize).toFloat()
            }
            val lp = generateDefaultLayoutParams()
            lp.addRule(CENTER_VERTICAL)
            lp.marginStart = navContentMargin.toInt()
            navTextView?.layoutParams = lp
            addView(navTextView)

        }
        if(content.isNullOrEmpty()){
            navTextView?.text = ""
        }else{
            navTextView?.text = content
        }

    }


    fun setNavigationTextColor(color: Int){
        navTextView?.setTextColor(color)
    }

    fun setNavigationTextColor(color: ColorStateList){
        navTextView?.setTextColor(color)
    }

    fun setNavigationTextSize(size:Float){
        navTextView?.textSize = size
    }


    private fun setNavIcon(drawable: Drawable){
        if(navButton == null){
            navButton = AppCompatImageView(context)
            navButton?.setBackgroundColor(Color.TRANSPARENT)
            val lp = generateDefaultLayoutParams()
            lp.addRule(CENTER_VERTICAL)
            lp.marginStart = navDefaultMargin.toInt()
            navButton?.layoutParams = lp
            addView(navButton)
        }
        val size = dp2px(context,navIconSize.toFloat())
        drawable.setBounds(0,0,size,size)
        navButton?.setImageDrawable(drawable)
        navButton?.layoutParams?.width  = size
        navButton?.layoutParams?.height  = size
        //navButton?.setCompoundDrawables(drawable,null,null,null)
        navButton?.setOnClickListener {
            onNavigationClick?.invoke(it)
        }
    }


    fun setNavigationIcon(drawable: Drawable){
        setNavIcon(drawable)
    }

    fun setNavigationTint(color: Int){
        setNavigationTintList(ColorStateList.valueOf(color))
    }

    fun setNavigationTintList(color: ColorStateList){
        navIconDrawable?.setTintList(color)
    }


    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }


    private fun shouldLayout(view: View?): Boolean {
        return view != null && view.parent === this && view.visibility != View.GONE
    }


    private fun getHorizontalMargins(v: View): Int {
        val mlp = v.layoutParams as MarginLayoutParams
        return MarginLayoutParamsCompat.getMarginStart(mlp) + MarginLayoutParamsCompat.getMarginEnd(mlp)
    }
}