package com.showmethe.wanandroid.ui.project.adapter

import android.content.Context
import android.view.View
import androidx.databinding.ObservableArrayList



import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ItemProjectBinding
import com.showmethe.wanandroid.entity.CateBean
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.glide.loadScaleNoCrop


class ProjectAdapter(context: Context, data: ObservableArrayList<CateBean.DatasBean>) :
    DataBindBaseAdapter<CateBean.DatasBean, ItemProjectBinding>(context, data) {
    override fun getItemLayout(): Int  = R.layout.item_project

    override fun bindItems(binding: ItemProjectBinding?, item: CateBean.DatasBean, position: Int) {
        binding?.apply {

            ivLogo.loadScaleNoCrop(item.envelopePic)

            bean  = item
            executePendingBindings()

            ivLogo.setOnClickListener {
                onImgClick?.invoke(it,item.envelopePic)
            }


            like.setLike(item.isCollect,false)
            like.setOnClickListener {
                if(item.isCollect){
                    like.setLike(false,false)
                }else{
                    like.setLike(true,true)
                }
                item.isCollect = !item.isCollect
                onLikeClick?.invoke(item,item.isCollect,position)
            }

        }
    }

    private var onImgClick :((view: View, item:String)->Unit)? = null
    fun setOnImgClickListener(onImgClick :((view: View, item:String)->Unit)){
        this.onImgClick = onImgClick
    }

    private var onLikeClick :((item: CateBean.DatasBean,isCollect:Boolean,pos:Int)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: CateBean.DatasBean,isCollect:Boolean,pos:Int)->Unit)){
        this.onLikeClick = onLikeClick
    }

}