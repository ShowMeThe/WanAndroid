package com.showmethe.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.showmethe.wanandroid.entity.Collect

import com.showmethe.wanandroid.ui.main.repostory.MainRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath

/**
 * com.ken.materialwanandroid.ui.main.vm
 *
 * 2019/9/9
 **/
class CollectViewModel(application: Application) : BaseViewModel(application) {

    @InjectOwner
    val repository = MainRepository()
    val collect = MutableLiveData<Collect>()

    override fun onViewModelCreated(owner: LifecycleOwner) {

    }


    /**
     * Collect
     */
    @VMPath(path = "getCollect")
    fun getCollect(pager:Int) = repository.getCollect(pager,collect)



    /**
     * Collect
     */
    @VMPath(path = "homeUnCollect")
    fun homeUnCollect(id:Int,originId:Int) = repository.unCollect(id,originId)
}