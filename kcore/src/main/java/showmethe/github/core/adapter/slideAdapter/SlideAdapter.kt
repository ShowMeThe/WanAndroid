package showmethe.github.core.adapter.slideAdapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import showmethe.github.core.R

import java.util.ArrayList
import showmethe.github.core.util.widget.ScreenSizeUtil
import androidx.recyclerview.widget.GridLayoutManager
import showmethe.github.core.adapter.addCallback


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter.slideAdapter
 */
abstract class SlideAdapter<D>(var mContext: Context,
                                                            var mData: ObservableArrayList<D>) : RecyclerView.Adapter<SlideViewHolder>() {
    private var mSlideItems: SlideItem? = null
    private var mRecycleView: RecyclerView? = null
    private var contentView: View? = null
    private var rightMenu: List<View>? = null

    //侧滑相关
    private var mOpenItem: SlideLayout? = null
    internal var scrollingItem: SlideLayout? = null

    private var click: OnSlideClickListener? = null
    private var layoutId = -1
    private var contentWidth  = 0

    init {
        mData.addCallback(this)
    }

    fun holdOpenItem(openItem: SlideLayout) {
        mOpenItem = openItem
    }

    internal fun closeOpenItem() {
        if (mOpenItem != null && mOpenItem!!.isOpen) {
            mOpenItem?.close()
            mOpenItem = null
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SlideViewHolder {
        val holder = SlideViewHolder(inflateItemView(parent))
        initRightMenu(holder)
        return holder
    }

    abstract fun  getItemLayout(): Int

    abstract fun bindItems(holder: SlideViewHolder, item: D, position: Int)

    private fun inflateItemView(viewGroup: ViewGroup): View {
        this.layoutId = getItemLayout()
        contentView = LayoutInflater.from(viewGroup.context).inflate(this.layoutId, viewGroup, false)
        return create(viewGroup.context,viewGroup,contentView!!)
    }


    override fun onBindViewHolder(viewHolder: SlideViewHolder, position: Int) {
        contentView?.setOnClickListener {
            click?.onContentItemClick(position)
        }

        bindItems(viewHolder, mData[position], position)
    }


   private fun create(context: Context, parent: ViewGroup,contentView:View): View {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_slide_layout, parent, false)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.slideLayout_ll_container)
        var rightMenu: View?

        this.contentView = contentView
        val params = linearLayout.layoutParams


        params.width = contentWidth
        linearLayout.addView(contentView,params)

        val rightList = ArrayList<View>()
        for (item in mSlideItems!!.menuItemList) {
            when (item.type) {
                MenuType.TEXT -> {
                    rightMenu = LayoutInflater.from(context).inflate(R.layout.item_slide_right_layout, linearLayout, false)
                    linearLayout.addView(rightMenu)
                    rightList.add(rightMenu)
                }
                MenuType.IMAGE -> {
                    rightMenu = LayoutInflater.from(context).inflate(R.layout.item_slide_right_image, linearLayout, false)
                    linearLayout.addView(rightMenu)
                    rightList.add(rightMenu)
                }
            }

        }
        this.rightMenu = rightList
        return itemView
    }


    private fun initRightMenu(holder: SlideViewHolder) {
        val item = mSlideItems

        for (i in rightMenu!!.indices) {
            val rightMenu = rightMenu!![i]
            val rightMenuParams = rightMenu.layoutParams as LinearLayout.LayoutParams
            rightMenuParams.width = (ScreenSizeUtil.width * item!!.rightMenuRatio).toInt()
            rightMenu.layoutParams = rightMenuParams

            rightMenu.setOnClickListener {
                if (click != null) {
                    closeOpenItem()
                    click?.onMenuItemClick(holder.layoutPosition,i)
                }
            }

            when (mSlideItems!!.menuItemList[i].type) {
                MenuType.IMAGE -> {
                    holder.setImageRes(mSlideItems!!.menuItemList[i].resId)
                    if (mSlideItems!!.menuItemList[i].backgroundColor != 0) {
                        holder.setBackgroundColor(mContext, mSlideItems!!.menuItemList[i].backgroundColor, MenuType.IMAGE)
                    }
                }
                MenuType.TEXT -> {
                    holder.setText(mSlideItems!!.menuItemList[i].stringText)
                    if (mSlideItems!!.menuItemList[i].parseTextColor != 0) {
                        holder.setTextColor(mContext, mSlideItems!!.menuItemList[i].parseTextColor)
                    }

                    if (mSlideItems!!.menuItemList[i].stringTextSize != 0f) {
                        holder.setTextSize(mSlideItems!!.menuItemList[i].stringTextSize)
                    }

                    if (mSlideItems!!.menuItemList[i].backgroundColor != 0) {
                        holder.setBackgroundColor(mContext, mSlideItems!!.menuItemList[i].backgroundColor, MenuType.TEXT)
                    }
                }
            }
            (holder.getView<View>(R.id.slideLayout) as SlideLayout).setRightMenuWidth(rightMenuParams.width)
        }
    }



    fun setOnSlideClickListener(clickListener: OnSlideClickListener) {
        this.click = clickListener
    }


    interface OnSlideClickListener {

        fun onContentItemClick(position: Int)

        fun onMenuItemClick(contentPos :Int,menuPosition: Int)

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecycleView = recyclerView
        mRecycleView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                scrollingItem = null
                closeOpenItem()
            }
        })


        val layoutParams = mRecycleView!!.getLayoutParams()
        val recyclerViewPadding = mRecycleView!!.paddingLeft + mRecycleView!!.paddingRight
        var recyclerViewMargin = 0
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            recyclerViewMargin = layoutParams.leftMargin + layoutParams.rightMargin
        }
        val layoutManager = mRecycleView!!.layoutManager
        if (layoutManager is LinearLayoutManager) {
            contentWidth = ScreenSizeUtil.width
        }
        if (layoutManager is GridLayoutManager) {
            contentWidth = ((ScreenSizeUtil.width) - recyclerViewMargin - recyclerViewPadding)/ layoutManager.spanCount
        }
    }



    fun setSlideItems(mSlideItems: SlideItem) {
        this.mSlideItems = mSlideItems
    }


    override fun getItemCount(): Int {
        return mData.size
    }
}
