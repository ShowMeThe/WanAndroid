package com.showmethe.wanandroid.ui.home.adapter


import android.content.Context
import androidx.databinding.ObservableArrayList



import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ItemHomeArticleBinding
import com.showmethe.wanandroid.entity.HomeArticle
import showmethe.github.core.adapter.DataBindBaseAdapter

class HomeArticleAdapter(context: Context, data: ObservableArrayList<HomeArticle.DatasBean>) :
    DataBindBaseAdapter<HomeArticle.DatasBean, ItemHomeArticleBinding>(context, data) {
    override fun getItemLayout(): Int = R.layout.item_home_article

    override fun bindItems(binding: ItemHomeArticleBinding?, item: HomeArticle.DatasBean, position: Int) {
        binding?.apply {
            bean = item

            like.setLike(item.isCollect,false)
            like.setOnClickListener {
                if(item.isCollect){
                    like.setLike(false,false)
                }else{
                    like.setLike(true,true)
                }
                item.isCollect = !item.isCollect
                onLikeClick?.invoke(item,item.isCollect)
            }
            executePendingBindings()
        }
    }

    private var onLikeClick :((item: HomeArticle.DatasBean,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: HomeArticle.DatasBean,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }

}