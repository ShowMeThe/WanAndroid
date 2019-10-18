package com.showmethe.wanandroid.ui.nav.adapter

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import com.google.android.flexbox.FlexboxLayout


import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ItemTreeBinding
import com.showmethe.wanandroid.entity.Tree
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.util.widget.px2sp
import java.util.concurrent.ThreadLocalRandom


class TreeAdapter(context: Context, data: ObservableArrayList<Tree>) :
    DataBindBaseAdapter<Tree, ItemTreeBinding>(context, data) {

    private val colors = arrayListOf("#ff80ab","#6200ea","#ff8a80","#d50000","#0091EA")

    override fun getItemLayout(): Int = R.layout.item_tree
    override fun bindItems(binding: ItemTreeBinding?, item: Tree, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()

            flex.removeAllViews()
            item.children.forEach {
                createTx(it.name,context,flex)
            }
        }
    }


    private fun createTx(item:String,context: Context,flexboxLayout: FlexboxLayout){
        val text = TextView(context)
        text.text = item
        text.textSize = px2sp(context,34f)
        val param = FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT,FlexboxLayout.LayoutParams.WRAP_CONTENT)
        param.setMargins(15,10,15,5)
        text.layoutParams = param
        text.setTextColor(Color.parseColor(colors[ThreadLocalRandom.current().nextInt(0, colors.size)]))
        flexboxLayout.addView(text)
    }

}