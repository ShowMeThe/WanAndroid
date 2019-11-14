package com.showmethe.wanandroid.ui.auth.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.ken.materialwanandroid.entity.Auth
import com.ken.materialwanandroid.entity.Empty
import com.showmethe.wanandroid.entity.RegisterBean
import com.showmethe.wanandroid.ui.auth.repository.AuthRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath
import showmethe.github.core.http.coroutines.Result



class AuthViewModel(application: Application) : BaseViewModel(application) {

    @InjectOwner
    val repository = AuthRepository()

    val registerBean  = RegisterBean()
    val register = MutableLiveData<Result<Empty>>()
    val auth = MutableLiveData<Result<Auth>>()
    val toNext = MutableLiveData<Int>()

    override fun onViewModelCreated(owner: LifecycleOwner) {

    }

    /**
     * 登录
     */
    @VMPath("login")
    fun login(username:String,password:String){
        repository.login(username, password, auth)
    }

    /**
     * 注册
     */
    @VMPath("register")
    fun register(){
        repository.register(registerBean.account, registerBean.password, register)
    }
}