package com.showmethe.wanandroid.ui.account.adapter


import android.content.Context
import androidx.databinding.ObservableArrayList



import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ItemArticleBinding
import com.showmethe.wanandroid.entity.Article
import showmethe.github.core.adapter.AutoLoadAdapter

class ArticleAdapter(context: Context, data: ObservableArrayList<Article.DatasBean>) :
    AutoLoadAdapter<Article.DatasBean, ItemArticleBinding>(context, data) {
    override fun getItemLayout(): Int = R.layout.item_article

    override fun bindItems(binding: ItemArticleBinding?, item: Article.DatasBean, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()


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

        }
    }


    private var onLikeClick :((item: Article.DatasBean,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: Article.DatasBean,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }
}