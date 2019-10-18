package com.ken.materialwanandroid.ui.home.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

import com.showmethe.wanandroid.entity.HomeArticle
import com.showmethe.wanandroid.ui.main.repostory.MainRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath
import showmethe.github.core.http.coroutines.Result


class SearchViewModel(application: Application) : BaseViewModel(application) {

    @InjectOwner
    val respoitory = MainRepository()
    val search = MutableLiveData<Result<HomeArticle>>()

    override fun onViewModelCreated(owner: LifecycleOwner) {

    }

    /**
     * 搜索
     */
    @VMPath("search")
    fun search(keyWord:String,page:Int){
        respoitory.search(keyWord,page,search)
    }

}