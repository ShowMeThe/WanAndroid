package showmethe.github.core.widget.common

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

import java.lang.ref.WeakReference
import java.util.ArrayList
import showmethe.github.core.R

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:52
 * Package Name:showmethe.github.core.widget.common
 * 需要包裹在NestedScrollView外或者使用NestedScrollView 时候高度Match_parent 并设置android:fillViewport="true"
 */
class SmartLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {


    private var loadingView: View? = null
    private var emptyView: View? = null
    private var errorView: View? = null
    private var loadinglayout = loadingId
    private var emptyLayout = emptyId
    private var errorLayout = errorId

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private val views = ArrayList<View>()


       private val mHandler = Handler(WeakReference<Handler.Callback>(Handler.Callback {
        when (it.what) {
            0 -> setViewState(loadingState)
            1 -> setViewState(emptyState)
            2 -> setViewState(errorState)
            4 -> setViewState(contentState)
        }
        true
    }).get())

    init {
        init(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec)

    }

    interface DefaultLayoutCreator {

        fun createLoadingView(): Int

        fun createErrorView(): Int

        fun createEmptyView(): Int

    }

    private fun init(context: Context) {
        if (creator != null) {
            loadinglayout = if (creator!!.createLoadingView() <= 0) loadingId else creator!!.createLoadingView()
            emptyLayout = if (creator!!.createEmptyView() <= 0) emptyId else creator!!.createEmptyView()
            errorLayout = if (creator!!.createErrorView() <= 0) errorId else creator!!.createErrorView()
        }

        DEFAULT_LAYOUT_PARAMS.gravity = Gravity.CENTER


        loadingView = LayoutInflater.from(context).inflate(loadinglayout, null)
        loadingView!!.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(loadingView!!)


        emptyView = LayoutInflater.from(context).inflate(emptyLayout, null)
        emptyView!!.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(emptyView!!)


        errorView = LayoutInflater.from(context).inflate(errorLayout, null)
        errorView!!.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(errorView!!)

    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        addViewInLayout(loadingView, -1, DEFAULT_LAYOUT_PARAMS, true)
        addViewInLayout(emptyView, -1, DEFAULT_LAYOUT_PARAMS, true)
        addViewInLayout(errorView, -1, DEFAULT_LAYOUT_PARAMS, true)

        setViewState(contentState)
    }



    fun showLoading() {
        handler.sendEmptyMessageDelayed(0, 200)
    }

    fun showEmpty() {
        mHandler.sendEmptyMessageDelayed(1, 200)
    }

    fun showError() {
        mHandler.sendEmptyMessageDelayed(2, 200)
    }

    fun showContent(rootLayout: View) {
        mHandler.sendEmptyMessageDelayed(4, 200)
    }

    fun showContent() {
        mHandler.sendEmptyMessageDelayed(4, 200)
    }


    fun hideButton() {
        for (i in 0 until childCount) {
            if (getChildAt(i) is Button) {
                (getChildAt(i) as Button).stateListAnimator = null
            }
        }
    }


    private fun setViewState(state: Int) {

        if (state == contentState) {
            errorView!!.visibility = View.GONE
            emptyView!!.visibility = View.GONE
            loadingView!!.visibility = View.GONE
        }

        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (state == contentState && view != loadingView && view != errorView && view != emptyView) {
                view.visibility = View.VISIBLE
            } else {
                if (state == loadingState && view == loadingView) {
                    view.visibility = View.VISIBLE
                } else if (state == errorState && view == errorView) {
                    view.visibility = View.VISIBLE
                } else if (state == emptyState) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }
        }

    }

    companion object {


        private val loadingId = R.layout.smart_loading_layout
        private val emptyId = R.layout.smart_empty_layout
        private val errorId = R.layout.smart_error_layout

        private val loadingState = 0
        private val emptyState = 1
        private val errorState = 2
        private val contentState = 4

        private var creator: DefaultLayoutCreator? = null
        fun setDefaultLayoutCreator(defaultLayoutCreator: DefaultLayoutCreator) {
            creator = defaultLayoutCreator
        }

        private val DEFAULT_LAYOUT_PARAMS = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
    }

}
