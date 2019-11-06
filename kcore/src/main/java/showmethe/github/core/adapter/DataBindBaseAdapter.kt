package showmethe.github.core.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter
 */

abstract class DataBindBaseAdapter<D, V : ViewDataBinding>(var context: Context,
                                                           var data: ObservableArrayList<D>)
    : RecyclerView.Adapter<DataBindingViewHolder<V>>() {




    init {
        this.data.addCallback(this)
    }


    override fun getItemId(position: Int): Long {
        return if(data.size>0) data[position].hashCode().toLong() else 0
    }



    override fun onBindViewHolder(holder: DataBindingViewHolder<V>, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClick?.apply {
                invoke(it,holder.layoutPosition)
            }
        }
        bindItems(holder.binding, data[position], position)
    }

    abstract fun getItemLayout() : Int

    abstract fun bindItems(binding: V?, item: D, position: Int)



    fun inflateItemView(viewGroup: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<V> {
        val binding = DataBindingUtil.bind<V>(inflateItemView(parent, getItemLayout()))
        return DataBindingViewHolder<V>(binding!!)
    }


    override fun getItemCount(): Int = data.size

    var onItemClick : ((view: View, position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener:(view: View, position: Int) -> Unit) {
        this.onItemClick = onItemClickListener
    }




}
