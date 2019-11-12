package showmethe.github.core.widget.common

import android.content.Context
import android.content.res.ColorStateList
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat

import java.lang.ref.WeakReference
import java.util.ArrayList

import showmethe.github.core.R


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:53
 * Package Name:showmethe.github.core.widget.common
 * 需要包裹在NestedScrollView外或者使用NestedScrollView 时候高度Match_parent 并设置android:fillViewport="true"
 */
class SmartRelativeLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {



    private var loadingView: View? = null
    private var emptyView: View? = null
    private var errorView: View? = null
    private var loadinglayout = loadingId
    private var emptyLayout = emptyId
    private var errorLayout = errorId
    private var onReload : (()->Unit)? = null
    private var currentState = 0
    private var progressbar :ProgressBar? = null

    private val views = ArrayList<View>()


    private val mHandler = Handler(WeakReference<Handler.Callback>(Handler.Callback {
        when (it.what) {
            0 -> setViewState(LOADING_STATE)
            1 -> setViewState(EMPTY_STATE)
            2 -> setViewState(ERROR_STATE)
            4 -> setViewState(CONTENT_STATE)
        }
        true
    }).get())



    interface DefaultLayoutCreator {

        fun createLoadingView(): Int

        fun createErrorView(): Int

        fun createEmptyView(): Int

    }

    init {
        init(context)
    }


    private fun init(context: Context) {

        if (creator != null) {
            loadinglayout = if (creator!!.createLoadingView() <= 0) loadingId else creator!!.createLoadingView()
            emptyLayout = if (creator!!.createEmptyView() <= 0) emptyId else creator!!.createEmptyView()
            errorLayout = if (creator!!.createErrorView() <= 0) errorId else creator!!.createErrorView()
        }


        loadingView = LayoutInflater.from(context).inflate(loadinglayout, null)
        loadingView!!.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(loadingView!!)

        emptyView = LayoutInflater.from(context).inflate(emptyLayout, null)
        emptyView!!.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(emptyView!!)

        errorView = LayoutInflater.from(context).inflate(errorLayout, null)
        errorView!!.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(errorView!!)

        loadingView?.isClickable = true
        loadingView?.setOnClickListener { }

        errorView?.isClickable = true
        errorView?.setOnClickListener {
            if(currentState == ERROR_STATE){
                onReload?.invoke()
                mHandler.sendEmptyMessageDelayed(0, 200)
            }
        }

        emptyView?.setOnClickListener {
            if(currentState == EMPTY_STATE){
                onReload?.invoke()
                mHandler.sendEmptyMessageDelayed(0, 200)
            }
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        addViewInLayout(errorView, -1, DEFAULT_LAYOUT_PARAMS, true)
        addViewInLayout(emptyView, -1, DEFAULT_LAYOUT_PARAMS, true)
        addViewInLayout(loadingView, -1, DEFAULT_LAYOUT_PARAMS, true)

        setViewState(LOADING_STATE)
    }


    fun setOnReloadWhenErrorOrEmpty(onReload:()->Unit){
        this.onReload = onReload
    }

    fun showLoading() {
        mHandler.sendEmptyMessageDelayed(0, 200)
    }

    fun showEmpty() {
        mHandler.sendEmptyMessageDelayed(1, 200)
    }

    fun showError() {
        mHandler.sendEmptyMessageDelayed(2, 200)
    }

    fun showContent() {
        mHandler.sendEmptyMessageDelayed(4, 200)
    }


    /**
     * 当需要遮盖Button需要设置这个
     */
    fun hideButton() {
        for (i in 0 until childCount) {
            if (getChildAt(i) is Button) {
                (getChildAt(i) as Button).stateListAnimator = null
            }
        }
    }


    private fun setViewState(state: Int) {
        this.currentState = state
        for (i in views.indices) {
            if (state == i) {
                views[i].visibility = View.VISIBLE
            } else {
                views[i].visibility = View.GONE
            }
        }

    }


    fun setDefaultLoadingColorRes(color: Int){
        loadingView?.findViewById<ProgressBar>(R.id.progressbar)?.indeterminateTintList = ColorStateList.valueOf(ContextCompat.getColor(context,color))
    }


    fun setDefaultLoadingColor(color: Int){
        loadingView?.findViewById<ProgressBar>(R.id.progressbar)?.indeterminateTintList = ColorStateList.valueOf(color)
    }

    companion object {


        private val loadingId = R.layout.smart_loading_layout
        private val emptyId = R.layout.smart_empty_layout
        private val errorId = R.layout.smart_error_layout
        public const val LOADING_STATE = 0
        public const val EMPTY_STATE = 1
        public const val ERROR_STATE = 2
        public const val CONTENT_STATE = 4


        private var creator: DefaultLayoutCreator? = null
        fun setDefaultLayoutCreator(defaultLayoutCreator: DefaultLayoutCreator) {
            creator = defaultLayoutCreator
        }

        private val DEFAULT_LAYOUT_PARAMS = LayoutParams(
            LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT)
    }


}
