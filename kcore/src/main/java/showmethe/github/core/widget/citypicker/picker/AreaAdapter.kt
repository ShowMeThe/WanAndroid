package showmethe.github.core.widget.citypicker.picker

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import showmethe.github.core.R
import showmethe.github.core.adapter.BaseRecyclerViewAdapter
import showmethe.github.core.widget.citypicker.bean.DistrictBean


import showmethe.github.core.widget.citypicker.picker.Const.INDEX_INVALID

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:53
 * Package Name:showmethe.github.core.widget.citypicker.picker
 */

class AreaAdapter(context: Context, mData: ObservableArrayList<DistrictBean>) :
    BaseRecyclerViewAdapter<DistrictBean, AreaAdapter.ViewHolder>(context, mData) {


    var selectedPosition = INDEX_INVALID
        private set

    fun updateSelectedPosition(index: Int) {
        val last = selectedPosition
        this.selectedPosition = index
        notifyItemChanged(index)
        notifyItemChanged(last)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateItemView(parent, R.layout.pop_citypicker_item))
    }

    override fun getItemId(position: Int): Long {
        return java.lang.Long.parseLong(mData[position].id)
    }


    override fun bindDataToItemView(holder: ViewHolder, item: DistrictBean, position: Int) {
        holder.name.text = item.name
        val checked = selectedPosition != INDEX_INVALID && mData[selectedPosition].id == item.id
        holder.name.isEnabled = checked
        holder.selectImg.visibility = if (checked) View.VISIBLE else View.GONE
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var name: TextView = itemView.findViewById(R.id.name)
        internal var selectImg: ImageView = itemView.findViewById(R.id.selectImg)
    }
}
