package com.showmethe.wanandroid.api

import com.ken.materialwanandroid.entity.*
import com.showmethe.wanandroid.entity.*
import retrofit2.Response
import retrofit2.http.*
import showmethe.github.core.http.JsonResult

/**
 * com.ken.materialwanandroid.api
 *
 * 2019/9/4
 **/
interface main {

    /**
     *  退出登录
     */
    @GET("/user/logout/json")
    suspend  fun logout() : Response<JsonResult<Empty>>

    /**
     *  首页banner
     */
    @GET("/banner/json")
    suspend  fun banner() : Response<JsonResult<ArrayList<Banner>>>


    /**
     * 文章
     */
    @GET("/article/list/{pager}/json")
    suspend  fun getHomeArticle(@Path("pager") pager :Int ) : Response<JsonResult<HomeArticle>>


    /**
     * 文章Tab
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getChapters() : Response<JsonResult<ArrayList<TabBean>>>

    /**
     * Tab的文章
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getArticle(@Path("id") id:Int,@Path("page") page:Int) : Response<JsonResult<Article>>


    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    suspend  fun homeCollect(@Path("id") id:Int): Response<JsonResult<Empty>>


    @POST("/lg/collect/add/json")
    @FormUrlEncoded
    suspend fun addCoolect(@Field("title") title: String,
                                 @Field("author") author: String,
                                 @Field("link") link: String): Response<JsonResult<Empty>>


    /**
     * 取消收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun homeUnCollect(@Path("id") id:Int): Response<JsonResult<Empty>>

    /**
     * 取消收藏
     */
    @FormUrlEncoded
    @POST("/lg/uncollect/{id}/json")
    suspend fun unCollect(@Path("id") id:Int,@Field("originId") originId:Int): Response<JsonResult<Empty>>


    /**
     * 体系数据
     */
    @GET("/tree/json")
    suspend  fun getTree() : Response<JsonResult<ArrayList<Tree>>>


    /**
     *  项目分类
     */
    @GET("/project/tree/json")
    suspend  fun getCateTab() : Response<JsonResult<ArrayList<CateTab>>>


    /**
     *  项目列表数据
     */
    @GET("/project/list/{pager}/json")
    suspend  fun getCate(@Path("pager") pager:Int,@Query("cid") id:Int) : Response<JsonResult<CateBean>>

    /**
     *  收藏文章列表
     */
    @GET("/lg/collect/list/{pager}/json")
    suspend  fun getCollect(@Path("pager") pager:Int) : Response<JsonResult<Collect>>


    /**
     *  收藏文章列表
     */
    @FormUrlEncoded
    @POST("/article/query/{pager}/json")
    suspend  fun search(@Path("pager") pager:Int,@Field("k") keyWord:String) : Response<JsonResult<HomeArticle>>

}