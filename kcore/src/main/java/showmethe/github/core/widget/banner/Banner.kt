package showmethe.github.core.widget.banner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

import java.util.ArrayList

import showmethe.github.core.R
import showmethe.github.core.widget.transformer.AlphaScalePageTransformer
import showmethe.github.core.widget.transformer.ParallaxTransformer
import showmethe.github.core.widget.transformer.SpinnerTransformer


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:54
 * Package Name:showmethe.github.core.widget.banner
 */
class Banner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    private val imageList = ObservableArrayList<Any>()
    private var viewPager: ViewPager2? = null
    lateinit var adapter: BannerViewAdapter
    private var dotTabView: DotTabView? = null
    private var orientation = ViewPager2.ORIENTATION_HORIZONTAL
    private var autoPlay = false
    private var smoothType = true
    private var selectColor: Int = 0
    private var unselectColor: Int = 0

    private var delayTime = TIME
    private var showIndicator = true
    private var mMinHeight = 0f
    private var mMaxHeight = 0f
    private var scaleType = 1
    private var transformer: ViewPager2.PageTransformer? = null
    private var transformerType = -1
    private var indicatorGravity = 0
    private lateinit var factory: BannerFactory
    private var owner: LifecycleOwner? = null
    private var scrollType = REPEAT //3循环 4假无限
    private var dotWith = 10
    private var dotType = 0
    private var dotDistant = 10


    private val task = object : Runnable {
        override fun run() {
            if (factory.count > 1 && autoPlay) {
                if (smoothType) {
                    factory.toNextPage(this)
                }

            }
        }
    }


    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {


        initType(context, attrs)
        val view = LayoutInflater.from(context).inflate(layoutId, null)
        viewPager = view.findViewById(R.id.viewPager)
        dotTabView = view.findViewById(R.id.dot)

        when (scrollType) {
            REPEAT -> factory = SmoothFactory(viewPager!!)
            INFINITY -> factory = InfinityFactory(viewPager!!)
        }


        if (showIndicator) {
            dotTabView?.visibility = View.VISIBLE
            setIndicatorGravity()
        }

        adapter = BannerViewAdapter(context, imageList)
        viewPager?.adapter = adapter
        viewPager?.orientation = orientation

        if (scrollType == 3) {
            when (transformerType) {
                0 -> viewPager?.setPageTransformer(ParallaxTransformer())
                1 -> viewPager?.setPageTransformer(SpinnerTransformer())
                2 -> viewPager?.setPageTransformer(AlphaScalePageTransformer())
            }
        }

        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                factory.onPageSelected(position)
                when (scrollType) {
                    REPEAT -> onPageSelect?.invoke(position)
                    INFINITY -> {
                        if (position == 0) {
                            onPageSelect?.invoke(factory.count - 1)
                        } else if (position == factory.count - 1) {
                            onPageSelect?.invoke(0)
                        } else {
                            onPageSelect?.invoke(position - 1)
                        }
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                factory.onPageScrollStateChanged(state)
            }
        })

        adapter.setOnImageLoader(object : BannerViewAdapter.onImageLoader {
            override fun display(url: Any, imageView: ImageView) {
                imageView.maxHeight = mMaxHeight.toInt()
                imageView.minimumHeight = mMinHeight.toInt()
                when (scaleType) {
                    0 -> imageView.scaleType = ImageView.ScaleType.FIT_XY
                    1 -> imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                }
                loader?.apply {
                    invoke(url, imageView)
                }
            }
        })

        adapter.setOnItemClickListener { view, position ->
            if (scrollType == 4) {
                if (position > 0 && position < factory.count - 1) {
                    onPageClick?.invoke(view, position - 1)
                }
            } else {
                onPageClick?.invoke(view, position)
            }
        }

        addViewInLayout(view, -1, DEFAULT_LAYOUT_PARAMS, true)
    }

    private fun initType(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.Banner)
        autoPlay = array.getBoolean(R.styleable.Banner_autoPlay, true)
        selectColor = array.getColor(
            R.styleable.Banner_selected_color,
            ContextCompat.getColor(context, R.color.colorAccent)
        )
        unselectColor = array.getColor(
            R.styleable.Banner_unselected_color,
            ContextCompat.getColor(context, R.color.white_85)
        )
        mMinHeight = array.getDimension(
            R.styleable.Banner_imageMinHeight,
            resources.getDimension(R.dimen.px600dp)
        )
        mMaxHeight = array.getDimension(
            R.styleable.Banner_imageMaxHeight,
            resources.getDimension(R.dimen.px2500dp)
        )

        delayTime = array.getInt(R.styleable.Banner_delayTime, TIME)
        scaleType = array.getInt(R.styleable.Banner_imageScaleType, 0)
        showIndicator = array.getBoolean(R.styleable.Banner_showIndicator, true)
        orientation = array.getInt(R.styleable.Banner_pageOrientation, RecyclerView.HORIZONTAL)
        transformerType = array.getInt(R.styleable.Banner_transformer, -1)
        indicatorGravity = array.getInt(R.styleable.Banner_indicator_gravity, 0)
        scrollType = array.getInt(R.styleable.Banner_scrollType, REPEAT)
        dotWith = array.getDimension(R.styleable.Banner_dotWith, 10f).toInt()
        dotType = array.getInt(R.styleable.Banner_dotType, 0)
        dotDistant = array.getDimension(R.styleable.Banner_dotDistant, 10f).toInt()
        array.recycle()
    }

    private fun setIndicatorGravity() {
        if (showIndicator) {
            val params = dotTabView?.layoutParams!! as LayoutParams
            when (indicatorGravity) {
                0 -> params.addRule(CENTER_IN_PARENT)
                1 -> {
                    params.addRule(ALIGN_PARENT_START)
                    params.addRule(CENTER_VERTICAL)
                }
                2 -> {
                    params.addRule(ALIGN_PARENT_END)
                    params.addRule(CENTER_VERTICAL)
                }
            }
        }
    }

    fun setPageTransformer(transformer: ViewPager2.PageTransformer) {
        this.transformer = transformer
        viewPager?.setPageTransformer(transformer)
    }


    var onPageSelect: ((position: Int) -> Unit)? = null

    fun setOnPageSelectListener(onPageSelect: ((position: Int) -> Unit)) {
        this.onPageSelect = onPageSelect
    }


    var onPageClick: ((view: View, position: Int) -> Unit)? = null

    fun setOnPageClickListener(onPageClick: ((view: View, position: Int) -> Unit)) {
        this.onPageClick = onPageClick
    }


    fun setCurrentPosition(position: Int, smooth: Boolean) {
        viewPager?.post {
            if (scrollType == INFINITY) {
                if (position >= 0 && position < factory.count - 1) {
                    factory.toNextPage(position + 1, smooth)
                }
            } else {
                factory.toNextPage(position, smooth)
            }
        }
    }

    fun getBannerSize() = imageList.size

    fun play() {
        autoPlay = true
        if (imageList.size > 1) {
            factory.clearTask(task)
            factory.postTask(task, delayTime.toLong())
        }
    }


    fun stopPlay() {
        factory.clearTask(task)
    }


    fun resumePlay() {
        factory.count = imageList.size
        factory.clearTask(task)
        factory.postTask(task, delayTime.toLong())
    }


    override fun onStop(owner: LifecycleOwner) {
        stopPlay()
    }

    override fun onResume(owner: LifecycleOwner) {
        if (autoPlay) {
            resumePlay()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stopPlay()
        this.owner?.lifecycle?.removeObserver(this)
        this.owner = null
    }

    fun bindToLife(owner: LifecycleOwner) {
        this.owner = owner
        this.owner?.lifecycle?.addObserver(this)
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (factory.count == 0) super.dispatchTouchEvent(ev)
        factory.dispatchTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                parent.requestDisallowInterceptTouchEvent(false)
                if (autoPlay) {
                    resumePlay()
                }
            }
            MotionEvent.ACTION_DOWN -> {
                stopPlay()
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    private var loader: ((url: Any, imageView: ImageView) -> Unit)? = null


    fun setOnImageLoader(loader: (url: Any, imageView: ImageView) -> Unit) {
        this.loader = loader
    }

    fun addList(arrayList: ArrayList<*>) {
        stopPlay()
        imageList.clear()
        imageList.addAll(arrayList)
        if (scrollType == INFINITY) {
            imageList.add(0, arrayList[arrayList.size - 1])
            imageList.add(arrayList[0])
            factory.toNextPage(1, false)
        } else {
            factory.toNextPage(0, true)
        }
        factory.currentItem = 1

        factory.count = imageList.size
        viewPager?.offscreenPageLimit = factory.count
        factory.delayTime = delayTime.toLong()
        if (autoPlay) {
            play()
        }


        if (showIndicator && arrayList.size > 1) {
            dotTabView!!.setIndicatorRadius(dotWith, dotWith)
            dotTabView!!.setViewPager2(
                viewPager,
                arrayList.size,
                imageList.size,
                scrollType,
                dotDistant,
                dotType,
                selectColor,
                unselectColor
            )
        }
    }

    companion object {

        private val layoutId = R.layout.banner_layout
        private val DEFAULT_LAYOUT_PARAMS = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        private const val TIME = 3000
        const val REPEAT = 3
        const val INFINITY = 4
    }

}
