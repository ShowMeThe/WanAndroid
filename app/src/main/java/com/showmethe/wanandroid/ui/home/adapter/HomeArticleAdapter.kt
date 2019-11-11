package com.showmethe.wanandroid.ui.home.adapter


import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList



import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ItemHomeArticleBinding
import com.showmethe.wanandroid.entity.HomeArticle
import com.showmethe.wanandroid.placeholder.PlaceHolderManager
import showmethe.github.core.adapter.BaseRecyclerViewAdapter
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.adapter.DataBindingViewHolder

class HomeArticleAdapter(context: Context, data: ObservableArrayList<HomeArticle.DatasBean>) :
    BaseRecyclerViewAdapter<HomeArticle.DatasBean,HomeArticleAdapter.ViewHolder >(context, data) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.bind<ItemHomeArticleBinding>(inflateItemView(parent,R.layout.item_home_article))!!)
    }

    override fun bindDataToItemView(
        holder: ViewHolder,
        item: HomeArticle.DatasBean,
        position: Int
    ) {
       holder.binding.apply {
           bean = item

           if(!item.isShow){
               holder.holderManager.patchViews(tvTit,tvClass,tvTime)
               root.postDelayed({
                   holder.holderManager.clear()
                   item.isShow = true
               },500)
           }else{
               holder.holderManager.clear()
           }

            like.setLike(item.isCollect,false)
            like.setOnClickListener {
                if(item.isCollect){
                    like.setLike(boolean = false, state = false)
                }else{
                    like.setLike(boolean = true, state = true)
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


    class ViewHolder(binding: ItemHomeArticleBinding) :
        DataBindingViewHolder<ItemHomeArticleBinding>(binding){
        val holderManager = PlaceHolderManager()

    }

}