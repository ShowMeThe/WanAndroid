package showmethe.github.core.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter
 */
class DataBindingViewHolder<T : ViewDataBinding>(var binding: T) : RecyclerView.ViewHolder(binding.root)