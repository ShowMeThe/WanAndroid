package com.showmethe.wanandroid.ui.main.adapter

import android.content.Context
import android.view.View
import androidx.databinding.ObservableArrayList



import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.dataTime
import com.showmethe.wanandroid.entity.Collect
import kotlinx.android.synthetic.main.item_collect.view.*
import showmethe.github.core.adapter.slideAdapter.SlideAdapter
import showmethe.github.core.adapter.slideAdapter.SlideViewHolder
import showmethe.github.core.glide.TGlide
import showmethe.github.core.glide.loadRevealNoCrop
import showmethe.github.core.glide.loadScaleNoCrop


class CollectAdapter(mContext: Context, mData: ObservableArrayList<Collect.DatasBean>) :
    SlideAdapter<Collect.DatasBean>(mContext, mData) {
    override fun getItemLayout(): Int = R.layout.item_collect

    override fun bindItems(holder: SlideViewHolder, item: Collect.DatasBean, position: Int) {
        holder.itemView.apply {
            if(item.envelopePic.isNotEmpty()){
                ivLogo.visibility = View.VISIBLE
                ivLogo.loadRevealNoCrop(item.envelopePic)
            }else{
                ivLogo.visibility = View.GONE
            }


            tvTit.text = item.title
            tvAuthor.text = item.author
            tvTime.dataTime(item.publishTime)


        }
    }
}