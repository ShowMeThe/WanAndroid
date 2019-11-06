package showmethe.github.core.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter
 */
abstract class BaseRecyclerViewAdapter<D, V : RecyclerView.ViewHolder>(var context: Context,
                                                                                                    var mData: ObservableArrayList<D>) : RecyclerView.Adapter<V>() {

    private var onItemClickListener: OnItemClickListener? = null

    init {
        this.mData.addCallback(this)
    }

    @FunctionalInterface
     interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    open fun getItem(position: Int): D {
        return mData[position]
    }

    open fun inflateItemView(viewGroup: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }


    protected abstract fun bindDataToItemView(holder: V, item: D, position: Int)


    override fun getItemCount(): Int {
        return  mData.size
    }


    override fun onBindViewHolder(viewHolder: V, position: Int) {
        val layoutParams = viewHolder.itemView.layoutParams
        viewHolder.itemView.layoutParams = layoutParams
        val item = getItem(position)

        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener { v ->
                onItemClickListener!!.onItemClick(v, viewHolder.layoutPosition)
            }
        }

        try {
            bindDataToItemView(viewHolder, item, position)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

}

