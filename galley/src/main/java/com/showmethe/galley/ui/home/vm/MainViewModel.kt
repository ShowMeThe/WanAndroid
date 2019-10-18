package com.showmethe.galley.ui.home.vm

import android.app.Application
import android.service.autofill.UserData
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.showmethe.galley.database.dto.PhotoWallDto
import com.showmethe.galley.database.dto.UserDto
import com.showmethe.galley.entity.LoginBean
import com.showmethe.galley.ui.home.repository.MainRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 13:47
 * Package Name:com.showmethe.galley.ui.auth.vm
 */

class MainViewModel(application: Application) : BaseViewModel(application) {

    @InjectOwner
    val repository = MainRepository()

    val user = MutableLiveData<UserDto>()
    val bean  = MutableLiveData<List<PhotoWallDto>>()

    override fun onViewModelCreated(owner: LifecycleOwner) {

    }



    @VMPath(path = "getHomePhoto")
    fun getHomePhoto(){
        repository.getHomePhoto(bean)
    }


    @VMPath(path = "getUserByName")
    fun getUserByName(userName : String){
        repository.getUserByName(userName,user)
    }

}