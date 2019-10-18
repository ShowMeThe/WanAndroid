package com.showmethe.galley.ui.home.repository

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.showmethe.galley.database.DataSourceBuilder
import com.showmethe.galley.database.dto.PhotoWallDto
import showmethe.github.core.base.BaseRepository

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 16:19
 * Package Name:com.showmethe.galley.ui.home.repository
 */
class MainRepository() : BaseRepository() {

    val photoDao = DataSourceBuilder.getPhotoWall()


    fun getHomePhoto(bean : MutableLiveData<List<PhotoWallDto>>){
        photoDao.getPhotoBean().observe(owner!!, Observer {
            it?.apply {
                bean.postValue(this)
            }
        })
    }

}