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

            override fun onItemRangeInserted(sender: ObservableArrayList<D>, positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart,itemCount)
                notifyItemRangeChanged(positionStart, sender.size - positionStart)
            }


            override fun onItemRangeMoved(sender: ObservableArrayList<D>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (itemCount == 1) {
                    notifyItemMoved(fromPosition, toPosition)
                } else {
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeChanged(sender: ObservableArrayList<D>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)

            }

        })

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
