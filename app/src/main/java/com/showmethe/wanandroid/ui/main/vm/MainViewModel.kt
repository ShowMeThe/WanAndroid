package com.showmethe.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.showmethe.wanandroid.entity.*
import com.showmethe.wanandroid.ui.main.repostory.MainRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath
import showmethe.github.core.http.coroutines.Result

/**
 * com.ken.materialwanandroid.ui.main.vm
 *
 * 2019/9/4
 **/
class MainViewModel(application: Application) : BaseViewModel(application) {


    @InjectOwner
    val repository = MainRepository()


    var openDrawer = MutableLiveData<Boolean>()
    val banner = MutableLiveData<Result<ArrayList<Banner>>>()
    val tabs = MutableLiveData<Result<ArrayList<TabBean>>>()
    val article = MutableLiveData<Result<HomeArticle>>()
    val tree = MutableLiveData<Result<ArrayList<Tree>>>()
    val cateTab = MutableLiveData<Result<ArrayList<CateTab>>>()
    val cate = MutableLiveData<Result<CateBean>>()




    override fun onViewModelCreated(owner: LifecycleOwner) {


    }


    /**
     * logout
     */
    fun logout() = repository.logout()


    /**
     * Banner
     */
    @VMPath(path = "getBanner")
    fun getBanner() = repository.getBanner(banner)


    /**
     * Article
     */
    @VMPath(path = "getHomeArticle")
    fun getHomeArticle(pager:Int) = repository.getHomeArticle(pager,article)


    /**
     * Chapters
     */
    fun getChapters() = repository.getChapters(tabs)


    /**
     * Chapters
     */
    @VMPath(path = "getArticle")
    fun getArticle(id:Int,pager:Int,article:MutableLiveData<Result<Article>>) = repository.getArticle(id,pager,article)


    /**
     * Chapters
     */
    fun homeCollect(id:Int) = repository.homeCollect(id)

    fun addCollect(title: String,author: String,link: String) = repository.addCoolect(title, author, link)

    /**
     * Collect
     */
    fun homeUnCollect(id:Int) = repository.homeUnCollect(id)

    /**
     * Tree
     */
    @VMPath(path = "getTree")
    fun getTree() = repository.getTree(tree)


    /**
     * CateTab
     */
    @VMPath(path = "getCateTab")
    fun getCateTab() = repository.getCateTab(cateTab)


    /**
     * Cate
     */
    @VMPath(path = "getCate")
    fun getCate(pager:Int,id:Int) = repository.getCate(pager,id,cate)


}