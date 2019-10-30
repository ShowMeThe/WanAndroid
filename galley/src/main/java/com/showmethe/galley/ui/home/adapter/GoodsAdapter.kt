package com.showmethe.galley.ui.home.adapter

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.palette.graphics.Palette
import com.showmethe.galley.R
import com.showmethe.galley.database.dto.GoodsListDto
import com.showmethe.galley.databinding.ItemGoodsListBinding
import showmethe.github.core.adapter.AutoLoadAdapter
import showmethe.github.core.glide.TGlide

/**
 * Author: showMeThe
 * Update Time: 2019/10/19
 * Package Name:com.showmethe.galley.ui.home.adapter
 */
class GoodsAdapter(context: Context, data: ObservableArrayList<GoodsListDto>) :
    AutoLoadAdapter<GoodsListDto, ItemGoodsListBinding>(context, data) {

    private val defaultColor = ContextCompat.getColor(context, R.color.color_ff6e00)

    override fun bindItems(binding: ItemGoodsListBinding?, item: GoodsListDto, position: Int) {
        binding?.apply {
            bean  = item
            executePendingBindings()

            if(item.vibrantColor == -1){
                TGlide.loadIntoBitmap(item.coverImg){ bitmap ->
                    Palette.from(bitmap).generate {
                        it?.apply {
                            item.vibrantColor = getVibrantColor(defaultColor)
                            tvDes.setTextColor(item.vibrantColor)
                            cardView.strokeColor = item.vibrantColor
                            cardView.rippleColor = ColorStateList.valueOf(item.vibrantColor)
                            ivLogo.setBackgroundColor(item.vibrantColor)
                            btnBuy.setBackgroundColor(item.vibrantColor)
                        }
                    }
                }
            }else{
                tvDes.setTextColor(item.vibrantColor)
                cardView.strokeColor = item.vibrantColor
                cardView.rippleColor = ColorStateList.valueOf(item.vibrantColor)
                btnBuy.setBackgroundColor(item.vibrantColor)
                ivLogo.setBackgroundColor(item.vibrantColor)
            }

            btnBuy.setOnClickListener {
                onBuyClick?.invoke(item.id)
            }

        }

    }

    private var onBuyClick : ((id:Int)->Unit)? = null
    fun  setOnBuyClickListener( onBuyClick  : ((id:Int)->Unit)){
        this.onBuyClick  = onBuyClick
    }


    override fun getItemLayout(): Int  = R.layout.item_goods_list

}