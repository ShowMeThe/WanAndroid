package com.showmethe.wanandroid.ui.account.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.showmethe.wanandroid.constant.Id


import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.FragmentArticleBinding
import com.showmethe.wanandroid.entity.Article
import com.showmethe.wanandroid.ui.account.adapter.ArticleAdapter
import com.showmethe.wanandroid.ui.home.openDetail
import com.showmethe.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_article.*
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.widget.common.SmartRelativeLayout.Companion.CONTENT_STATE
import showmethe.github.core.widget.common.SmartRelativeLayout.Companion.EMPTY_STATE


class ArticleFragment : LazyFragment<FragmentArticleBinding, MainViewModel>() {


    companion object{

        fun get(id:Int) : ArticleFragment {
            val bundle = Bundle()
            bundle.putInt(Id,id)
            val fragment = ArticleFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    val contentState = MutableLiveData<Int>()
    val refreshing = MutableLiveData<Boolean>()
    private val list = ObservableArrayList<Article.DatasBean>()
    private lateinit var adapter: ArticleAdapter
    private val pagerNumber = MutableLiveData<Int>()
    private val article  = MutableLiveData<Result<Article>>()
    private var accountId = 0

    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)
    override fun getViewId(): Int = R.layout.fragment_article


    override fun onBundle(bundle: Bundle) {
        accountId = bundle.getInt(Id,0)

    }

    override fun observerUI() {

        pagerNumber.observe(this, Observer {
            it?.apply {
                router.toTarget("getArticle",accountId,this,article)
            }
        })


        article.observe(this, Observer {
            it?.apply {
                when(status){
                    Result.Success ->{
                        response?.apply {
                            if(pagerNumber.value!! == 0){
                                list.clear()
                            }

                            list.addAll(datas)
                            if(list.size!=0){
                                contentState.value = CONTENT_STATE
                            }else{
                                contentState.value = EMPTY_STATE
                            }
                            onSize(datas.size)
                        }
                    }
                    Result.OutTime ->{
                        adapter.loadingOverTime()
                    }
                }

            }
        })

        viewModel.callId.observe(this, Observer {
            it?.apply {
                if(this == accountId){
                    rv.scrollToPosition(0)
                }
            }
        })

    }

    override fun init() {
        refresh.setColorSchemeResources(R.color.color_304ffe)
        binding?.article = this
        adapter = ArticleAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)


        pagerNumber.value = 0
    }


    fun loadRefresh(){
        pagerNumber.value = 0
    }



    override fun initListener() {

        smrl.setOnReloadWhenErrorOrEmpty {
            pagerNumber.value = 0
        }


        adapter.setOnLoadMoreListener {
            pagerNumber.value = pagerNumber.value!! + 1
        }



        adapter.setOnLikeClickListener { item, isCollect ->
            if(isCollect){
                viewModel.homeCollect(item.id)
            }else{
                viewModel.homeUnCollect(item.id)
            }
        }

        adapter.setOnItemClickListener { view, position ->
            context.openDetail(list[position].link)
        }
    }


    private fun onSize(size:Int){
        refreshing.value  = false
        if(size == 0){
            adapter.setEnableLoadMore(false)
        }else{
            adapter.setEnableLoadMore(true)
        }
    }


}