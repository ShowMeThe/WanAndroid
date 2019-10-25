package com.showmethe.galley.ui.home.adapter

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.showmethe.galley.R
import com.showmethe.galley.database.dto.PhotoWallDto
import com.showmethe.galley.databinding.ItemPhotoBinding
import showmethe.github.core.adapter.BaseRecyclerViewAdapter
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.adapter.DataBindingViewHolder
import showmethe.github.core.glide.TGlide


class PhotoAdapter(context: Context, data: ObservableArrayList<PhotoWallDto>) :
    BaseRecyclerViewAdapter<PhotoWallDto, PhotoAdapter.ViewHolder>(context, data) {

    @SuppressLint("SetTextI18n")
    override fun bindDataToItemView(holder: ViewHolder, item: PhotoWallDto, position: Int) {
        holder.apply {
            binding.apply {
                bean = item
                executePendingBindings()

                if(adapter == null){
                    holder.list.addAll(item.imageList)
                    adapter = VpImgAdapter(context,holder.list)
                }else{
                    holder.list.clear()
                    holder.list.addAll(item.imageList)
                }

                like.setLike(item.isLike,false)
                like.setOnClickListener {
                    item.isLike = !item.isLike
                    like.setLike(item.isLike,true)
                    onPhotoLike?.invoke(item.id,item.isLike)
                }

                vp2.adapter = adapter
                vp2.setCurrentItem(item.selectPosition,false)
                tvSelect.text = "${item.selectPosition + 1}/${item.imageList.size}"
                vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        item.selectPosition  = position
                        tvSelect.text = "${item.selectPosition + 1}/${item.imageList.size}"
                    }
                })

                adapter?.setOnItemClickListener { view, position ->
                    onViewClick?.invoke(vp2,item.imageList[position])
                }

            }

        }
    }

    private var onPhotoLike :((id : Int,like:Boolean)->Unit)? = null
    fun setOnPhotoLikeListener(onPhotoLike :((id : Int,like:Boolean)->Unit)){
        this.onPhotoLike = onPhotoLike
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.bind<ItemPhotoBinding>(inflateItemView(parent,R.layout.item_photo))
        return ViewHolder(binding!!)
    }

    private  var onViewClick: ((view:View,url: String)->Unit)? = null

    fun setOnViewClickListener(onViewClick: ((view:View,url: String)->Unit)){
        this.onViewClick = onViewClick
    }

    private  var onLikeClick: ((id: Int, like:Boolean)->Unit)? = null

    fun setOnLikeClickListener(onLikeClick: ((id: Int, like:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }


    class ViewHolder(binding: ItemPhotoBinding) : DataBindingViewHolder<ItemPhotoBinding>(binding){
        var adapter : VpImgAdapter ?= null
        val list = ObservableArrayList<String>()

    }

}