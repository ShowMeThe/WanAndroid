package com.showmethe.wanandroid.ui.auth.repository

import androidx.lifecycle.MutableLiveData
import com.showmethe.wanandroid.api.auth
import com.ken.materialwanandroid.entity.Auth
import com.ken.materialwanandroid.entity.Empty
import com.showmethe.galley.database.AppDataBaseCreator
import com.showmethe.galley.database.DataSourceBuilder
import com.showmethe.galley.database.dto.UserDto
import com.showmethe.wanandroid.toast
import showmethe.github.core.base.BaseRepository
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.http.coroutines.CallResult
import showmethe.github.core.http.coroutines.Result


class AuthRepository : BaseRepository() {

    val api = RetroHttp.createApi(auth::class.java)
    val userDao = DataSourceBuilder.getUser()


    fun login(username:String,password:String,call:MutableLiveData<Result<Auth>>){
        CallResult<Auth>(owner)
            .loading {
                showLoading()
            }.success { result, message ->
                dismissLoading()
                call.value = result


                /**
                 * 本地数据库注册账号
                 */
                val userdto = UserDto()
                userdto.userName = username
                userdto.password = password
                userDao.register(userdto)

            }.error { result, code, message ->
                toast(code,message)
                dismissLoading()
                call.value = result
            }.outTime {
                dismissLoading()
                call.value = it
            }.hold {
                api.login(username, password)
            }
    }

    fun register(username:String,password:String,call:MutableLiveData<Result<Empty>>){
        CallResult<Empty>(owner)
            .loading {
                showLoading()
            }.success { result, message ->
                dismissLoading()
                call.value = result
            }.error { result, code, message ->
                dismissLoading()
                call.value = result
            }.outTime {
                dismissLoading()
                call.value = it
            }.hold {
                api.register(username, password,password)
            }
    }


}