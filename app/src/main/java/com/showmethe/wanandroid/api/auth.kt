package com.showmethe.wanandroid.api

import com.ken.materialwanandroid.entity.Auth
import com.ken.materialwanandroid.entity.Empty
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import showmethe.github.core.http.JsonResult

/**
 * com.ken.materialwanandroid.api
 *
 * 2019/9/4
 **/
interface auth {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username:String,@Field("password") password:String) : Response<JsonResult<Auth>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend  fun register(@Field("username") username:String,@Field("password") password:String,
                          @Field("repassword") repassword:String) : Response<JsonResult<Empty>>


    @Multipart
    @POST("user/upload")
    suspend  fun upload(@Part file: MultipartBody.Part) : Response<JsonResult<String>>
}