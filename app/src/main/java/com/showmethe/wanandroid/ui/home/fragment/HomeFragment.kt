package com.showmethe.wanandroid.ui.home.fragment

import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout


import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.constant.onPage
import com.showmethe.wanandroid.constant.onStartBanner
import com.showmethe.wanandroid.constant.onStopBanner
import com.showmethe.wanandroid.databinding.FragmentHomeBinding
import com.showmethe.wanandroid.entity.HomeArticle
import com.showmethe.wanandroid.expand.Builder
import com.showmethe.wanandroid.expand.ExpandIcon
import com.showmethe.wanandroid.expand.ExpandManager
import com.showmethe.wanandroid.ui.home.SearchActivity
import com.showmethe.wanandroid.ui.home.adapter.HomeArticleAdapter
import com.showmethe.wanandroid.ui.home.openDetail
import com.showmethe.wanandroid.ui.main.startToImgs
import com.showmethe.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.wanandroid.util.HoldViewHelper
import kotlinx.android.synthetic.main.fragment_accunt.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.toolbar
import kotlinx.android.synthetic.main.include_main_title.*
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.glide.TGlide
import showmethe.github.core.http.coroutines.Result.Companion.OutTime
import showmethe.github.core.http.coroutines.Result.Companion.Success
import showmethe.github.core.livebus.LiveBusHelper
import showmethe.github.core.util.widget.StatusBarUtil
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar
import showmethe.github.core.util.widget.setOnSingleClickListener
import kotlin.math.abs


class HomeFragment : LazyFragment<FragmentHomeBinding, MainViewModel>() {


    val refreshing = MutableLiveData<Boolean>()
    private val pagerNumber = MutableLiveData<Int>()
    private lateinit var adapter: HomeArticleAdapter
    private val list = ObservableArrayList<HomeArticle.DatasBean>()
    private  val bannerList  = ArrayList<String>()
    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)
    override fun getViewId(): Int = R.layout.fragment_home

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.banner.observe(this, Observer { bean ->
            bean?.apply {
                if(status == Success){
                    response?.apply {
                        bannerList.clear()
                        forEach {
                            bannerList.add(it.imagePath)
                        }
                        banner.addList(bannerList)
                    }
                }
            }
        })

        pagerNumber.observe(this, Observer {
            it?.apply {
                router.toTarget("getHomeArticle",this)
            }
        })

        viewModel.article.observe(this, Observer {
            it?.apply {
                when(status){
                    Success ->{
                        response?.apply {
                            if(pagerNumber.value!! == 1){
                                list.clear()
                            }
                            this.datas?.apply {
                                list.addAll(this)
                                onLoadSize(size)
                            }
                        }
                    }
                    OutTime-> {  //增加一处超时
                        rv.finishLoading()
                        refreshing.value = false
                    }
                }
            }
        })

    }




    override fun init() {
        fixToolbar(toolbar)
        refresh.setColorSchemeResources(R.color.colorAccent)
        rv.hideWhenScrolling(refresh)
        binding?.home = this

        refreshing.value = true

        val expands = ArrayList<ExpandIcon>()
        expands.add(ExpandIcon().setIcon(R.mipmap.baseline_search_white_24).setBackgroundTint(R.color.colorPrimaryDark))
        expands.add(ExpandIcon().setIcon(R.mipmap.baseline_arrow_upward_white_24dp).setBackgroundTint(R.color.colorPrimaryDark))
        ExpandManager.newBuilder().setExpandIcons(expands).motion(R.color.black,R.mipmap.close).bindTarget(crl).build()


        router.toTarget("getBanner")


        banner.setOnImageLoader { url, imageView ->
            TGlide.loadNoCrop(url, imageView)
        }
        banner.bindToLife(this)


        adapter = HomeArticleAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)


        pagerNumber.value = 0
    }


    override fun isLiveEventBusHere(): Boolean = true
    override fun onEventComing(helper: LiveBusHelper) {
        when(helper.code){
            onStopBanner ->  banner.stopPlay()
            onStartBanner -> banner.resumePlay()
            onPage ->{
                val pos = helper.data as Int
                banner.setCurrentPosition(pos,false)
            }
        }
    }



    fun loadMore(){
        pagerNumber.value =  pagerNumber.value!! + 1
    }

    fun loadRefresh(){
        pagerNumber.value = 0
    }


    override fun initListener() {

        crl.setOnMenuClickListener {
            when(it){
                0 ->{
                   context. startActivity<SearchActivity>()
                }
                1 ->{
                    rv.scrollToPosition(0)
                }
            }
        }


        profile.setOnSingleClickListener {
            viewModel.openDrawer.value = true
        }

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                //展开
                verticalOffset == 0 -> {
                    ivCover.alpha = 0f
                    toolbar.alpha = ivCover.alpha
                }
                abs(verticalOffset) >= appBarLayout.totalScrollRange -> //折叠
                {
                    ivCover.alpha = 1f
                    toolbar.alpha = ivCover.alpha
                }
                else -> {
                    //中间态
                    val alpha =
                        abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange.toFloat())
                    ivCover.alpha  = if(alpha<0.25) 0f else (alpha*1.5).toFloat()
                    toolbar.alpha = ivCover.alpha
                }
            }
        })


        tvSearch.setOnClickListener {
            context. startActivity(null,
                SearchActivity::class.java,android.util.Pair(tvSearch,tvSearch.transitionName))
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

        banner.setOnPageClickListener { view, position ->
            context.startToImgs(bannerList,position,banner)
            HoldViewHelper.get().holdView(banner)
        }


        context.setExitSharedElementCallback(object : SharedElementCallback(){
            override fun onCaptureSharedElementSnapshot(
                sharedElement: View?,
                viewToGlobalMatrix: Matrix?,
                screenBounds: RectF?
            ): Parcelable {
                sharedElement?.alpha = 1f
                return super.onCaptureSharedElementSnapshot(
                    sharedElement,
                    viewToGlobalMatrix,
                    screenBounds
                )
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        HoldViewHelper.get().clear()
    }

    private fun onLoadSize(size: Int) {
        rv.finishLoading()
        refreshing.value = false
        if(size == 0){
            rv.setEnableLoadMore(false)
        }else{
            rv.setEnableLoadMore(true)
        }
    }

}