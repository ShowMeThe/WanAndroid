package com.showmethe.galley.ui.home.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import com.showmethe.galley.R
import com.showmethe.galley.databinding.ItemCartListBinding
import com.showmethe.galley.entity.CartListBean
import showmethe.github.core.adapter.DataBindBaseAdapter

/**
 *  com.showmethe.galley.ui.home.adapter
 *  2019/10/31
 *  22:55
 */
class CartListAdapter(context: Context, data: ObservableArrayList<CartListBean>) :
    DataBindBaseAdapter<CartListBean, ItemCartListBinding>(context, data) {
    override fun getItemLayout(): Int = R.layout.item_cart_list

    override fun bindItems(binding: ItemCartListBinding?, item: CartListBean, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()
        }
    }
}