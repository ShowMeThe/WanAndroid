package showmethe.github.core.adapter


import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import android.os.Handler
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_adapter_footer.view.*
import showmethe.github.core.R

import java.lang.ref.WeakReference


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter
 */
abstract class AutoLoadAdapter<D, V : ViewDataBinding>(var context: Context,
                                                       var data: ObservableArrayList<D>) :
        RecyclerView.Adapter<DataBindingViewHolder<V>>()   {

    companion object {
        private const  val TYPE_COMMON_VIEW = 10001
        private const  val TYPE_FOOTER_VIEW = 10002
    }

    private var isLoading = false
    private var loadingFail = false
    private var canLoadMore = true

    private var footerView : View? = null
    private var lastPosition = 0
    private var rv : RecyclerView? = null
    private lateinit var lastPositions : IntArray
    private var staggeredGridLayoutManager : androidx.recyclerview.widget.StaggeredGridLayoutManager? = null
    private var gridManager : androidx.recyclerview.widget.GridLayoutManager? = null

    private val handle = Handler(WeakReference(Handler.Callback {
        when(it.what){
            0 ->{
                footerView?.apply {
                    rlLoading.postDelayed({
                        isLoading = false
                        rlLoading.visibility = View.GONE
                    },300)
                }

            }

            1 ->{
                footerView?.apply {
                    loadState.text = context.getString(R.string.loadingFailed)
                    rlLoading.postDelayed({
                        isLoading = false
                        rlLoading.visibility = View.GONE
                    },300)
                }
            }

            2 ->{
                footerView?.apply {
                    rlLoading.postDelayed({
                        rlLoading.visibility = View.VISIBLE
                        loadState.text = context.getString(R.string.no_more_data_to_read)
                        progressbar.visibility = View.GONE
                        isLoading = false
                    },350)
                }
            }

            3 ->{
                footerView?.apply {
                    progressbar.visibility = View.VISIBLE
                    isLoading = false
                }
            }
        }
        true
    }).get())

    init {


        this.data.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<D>>(){
            override fun onChanged(sender: ObservableArrayList<D>?) {
                notifyDataSetChanged()
            }
            override fun onItemRangeRemoved(sender: ObservableArrayList<D>?, positionStart: Int, itemCount: Int) {
                if (itemCount == 1) {
                    notifyItemRemoved(positionStart)
                    notifyItemRangeChanged(positionStart, itemCount)
                } else {
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeMoved(sender: ObservableArrayList<D>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (itemCount == 1) {
                    notifyItemMoved(fromPosition, toPosition)
                } else {
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeInserted(sender: ObservableArrayList<D>, positionStart: Int, itemCount: Int) {
                notifyItemInserted(positionStart + 1)
                notifyItemRangeChanged(positionStart , sender.size - positionStart)


            }

            override fun onItemRangeChanged(sender: ObservableArrayList<D>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

        })

    }


    private val listener  = object  : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(itemCount > 1){
                findLastPosition(recyclerView,dy)
            }
        }

    }

    private val sizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup(){
        override fun getSpanSize(p0: Int): Int {
            if (getItemViewType(p0) == TYPE_FOOTER_VIEW) {
                return gridManager!!.spanCount
            }
            return 1
        }
    }

    override fun onViewAttachedToWindow(holder: DataBindingViewHolder<V>) {
        super.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (getItemViewType(position) == TYPE_FOOTER_VIEW) {
            val lp = holder.itemView.layoutParams

            if (lp != null && lp is androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        recyclerView.apply {
            rv = this
            recyclerView.addOnScrollListener(listener)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        handle.removeCallbacksAndMessages(null)
    }

    private fun findLastPosition(recyclerView : RecyclerView, dy : Int){
        when (val layoutManager = recyclerView.layoutManager) {
            is androidx.recyclerview.widget.GridLayoutManager -> {
                lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                gridManager  = layoutManager
                gridManager?.apply {
                    spanSizeLookup = sizeLookup
                }
            }
            is androidx.recyclerview.widget.LinearLayoutManager -> lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            is androidx.recyclerview.widget.StaggeredGridLayoutManager -> {
                staggeredGridLayoutManager = layoutManager
                lastPositions =  staggeredGridLayoutManager?.findLastCompletelyVisibleItemPositions(lastPositions)!!
                lastPosition = findMax(lastPositions)

            }
            else -> throw RuntimeException("layoutManager not support")
        }

        if (canLoadMore){
            if((lastPosition >= itemCount - 1) && itemCount >= 1 && !isLoading ){
                isLoading = true
                footerView?.apply {
                    rv?.stopScroll()
                    loadState.text = context.getString(R.string.loading)
                    rlLoading.visibility = View.VISIBLE
                    loadState.text = context.getString(R.string.loading)
                }
                onLoadMore?.apply {
                    invoke()
                }
            }
        }

    }

    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }


    fun setEnableLoadMore(boolean: Boolean){
        canLoadMore  = boolean
        if(!canLoadMore){
            handle.sendEmptyMessage(2)
        }else{
            handle.sendEmptyMessage(3)
        }
    }

    fun finishLoading(){
        isLoading  = false
        handle.sendEmptyMessage(0)
    }

    fun loadingOverTime(){
        isLoading  = false
        handle.sendEmptyMessage(1)
    }

    abstract fun bindItems(binding: V?, item: D, position: Int)

    abstract fun getItemLayout() : Int

   private fun inflateItemView(viewGroup: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }


    override fun onBindViewHolder(holder: DataBindingViewHolder<V>, position: Int) {
        when(getItemViewType(position)){
            TYPE_COMMON_VIEW->{
                holder.itemView.setOnClickListener {
                    onItemClick?.apply {
                        invoke(it,holder.layoutPosition)
                    }
                }

                bindItems(holder.binding, data[position], position)
            }
            TYPE_FOOTER_VIEW ->{
                holder.itemView.apply {
                    if(canLoadMore){
                        loadState.text = context.getString(R.string.loading)
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<V> {
        var viewHolder : DataBindingViewHolder<V>?  = null
        when(viewType){
            TYPE_FOOTER_VIEW ->{
                footerView = inflateItemView(parent, R.layout.item_adapter_footer)
                viewHolder =  DataBindingViewHolder(DataBindingUtil.bind(footerView!!)!!)
            }

            TYPE_COMMON_VIEW ->{
                val binding = DataBindingUtil.bind<V>(inflateItemView(parent, getItemLayout()))
                viewHolder =  DataBindingViewHolder(binding!!)
            }
        }
       return viewHolder!!

    }

    override fun getItemViewType(position: Int): Int {
        if(position == itemCount -1 ){
            return  TYPE_FOOTER_VIEW
        }
        return  TYPE_COMMON_VIEW
    }

    var onLoadMore : (()-> Unit)? = null

    fun setOnLoadMoreListener(onLoadMore : (()-> Unit)){
        this.onLoadMore = onLoadMore
    }


    override fun getItemCount(): Int = data.size + 1

    var onItemClick : ((view: View, position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener:(view: View, position: Int) -> Unit) {
        this.onItemClick = onItemClickListener
    }



}