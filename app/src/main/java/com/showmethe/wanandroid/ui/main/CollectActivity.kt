package com.showmethe.wanandroid.ui.main

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ActivityCollectBinding
import com.showmethe.wanandroid.entity.Collect


import com.showmethe.wanandroid.ui.main.adapter.CollectAdapter
import com.showmethe.wanandroid.ui.main.vm.CollectViewModel
import kotlinx.android.synthetic.main.activity_collect.*
import showmethe.github.core.adapter.slideAdapter.SlideAdapter
import showmethe.github.core.adapter.slideAdapter.SlideCreator
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.divider.RecycleViewDivider


class CollectActivity : BaseActivity<ActivityCollectBinding, CollectViewModel>() {

    val refreshing = MutableLiveData<Boolean>()
    private val list = ObservableArrayList<Collect.DatasBean>()
    private lateinit var adapter: CollectAdapter
    private val pagerNumber = MutableLiveData<Int>()


    override fun getViewId(): Int = R.layout.activity_collect
    override fun initViewModel(): CollectViewModel = createViewModel(CollectViewModel::class.java)
    override fun onBundle(bundle: Bundle) {

    }



    override fun observerUI() {

        viewModel.collect.observe(this, Observer {
            it?.apply {
                if (pagerNumber.value!! == 0) {
                    list.clear()
                }
                this.datas?.apply {
                    smrl.showContent()
                    list.addAll(this)
                    onLoadSize(size)
                }
            }
        })

        pagerNumber.observe(this, Observer {
            it?.apply {
                router.toTarget("getCollect",this)
            }
        })


    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.collect = this
        refresh.setColorSchemeResources(R.color.colorPrimary)


        adapter = CollectAdapter(context, list)
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        rv.adapter = adapter
        rv.hideWhenScrolling(refresh)
        rv.addItemDecoration(RecycleViewDivider(RecyclerView.VERTICAL,RecyclerView.HORIZONTAL,1,ContextCompat.getColor(context,R.color.color_a100)))

        SlideCreator().addItemMenu(
            "取消收藏",
            R.color.white,
            R.color.color_ff3d00,15f)
            .bind(adapter, 0.2f)


        pagerNumber.value = 0
    }

    fun loadMore(){
        pagerNumber.value =   pagerNumber.value!! + 1
    }

    fun loadRefresh(){
        pagerNumber.value = 0
    }

    override fun initListener() {

        titleView.setOnNavigationClickListener {
            finishAfterTransition()
        }


        rv.setOnLoadMoreListener {
            pagerNumber.value = pagerNumber.value!! + 1
        }


        adapter.setOnSlideClickListener(object : SlideAdapter.OnSlideClickListener{
            override fun onMenuItemClick(contentPos: Int, menuPosition: Int) {
                val item = list[contentPos]
                router.toTarget("homeUnCollect",item.id,item.originId)
                list.removeAt(contentPos)
            }

            override fun onContentItemClick(position: Int) {

            }

        })
    }


    private fun onLoadSize(size: Int) {
        refresh.isRefreshing = false
        rv.finishLoading()
        if (size == 0) {
            rv.setEnableLoadMore(false)
        } else {
            rv.setEnableLoadMore(true)
        }
    }

}
