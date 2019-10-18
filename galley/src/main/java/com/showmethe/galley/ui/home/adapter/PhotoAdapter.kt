package com.showmethe.galley.ui.home.adapter

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.databinding.ObservableArrayList
import com.showmethe.galley.R
import com.showmethe.galley.database.dto.PhotoWallDto
import com.showmethe.galley.databinding.ItemPhotoBinding
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.glide.TGlide


class PhotoAdapter(context: Context, data: ObservableArrayList<PhotoWallDto>) :
    DataBindBaseAdapter<PhotoWallDto, ItemPhotoBinding>(context, data) {


    @SuppressLint("SetTextI18n")
    override fun bindItems(binding: ItemPhotoBinding?, item: PhotoWallDto, position: Int) {

        binding?.apply {
            bean = item
            executePendingBindings()

            banner.addList(item.imageList)

            banner.setCurrentPosition(item.selectPosition,true)
            banner.setOnImageLoader { url, imageView -> TGlide.loadNoCrop(url, imageView) }
            banner.setOnPageSelectListener {
                data[position].selectPosition = it
                tvSelect.text = "${data[position].selectPosition+1}/${item.imageList.size}"
            }


        }
    }

    private  var onLikeClick: ((id: Int, like:Boolean)->Unit)? = null

    fun setOnLikeClickListener(onLikeClick: ((id: Int, like:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }

    override fun getItemLayout(): Int = R.layout.item_photo
}