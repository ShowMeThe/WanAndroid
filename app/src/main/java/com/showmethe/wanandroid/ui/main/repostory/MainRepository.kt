package com.showmethe.wanandroid.ui.main.repostory

import androidx.lifecycle.MutableLiveData

import com.ken.materialwanandroid.entity.*
import com.showmethe.wanandroid.api.main
import com.showmethe.wanandroid.entity.*
import com.showmethe.wanandroid.toast
import showmethe.github.core.base.BaseRepository
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.http.coroutines.CallResult
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.kinit.get
import showmethe.github.core.kinit.inject
import showmethe.github.core.util.extras.set


class MainRepository : BaseRepository() {

    val api: main by inject()


    fun logout() {
        CallResult<Empty>(owner) {
            success { result, message ->

            }
            hold {
                api.logout()
            }
        }
    }

    fun getBanner(call: MutableLiveData<Result<ArrayList<Banner>>>) {

        CallResult<ArrayList<Banner>>(owner) {
            post(call)
            hold {
                api.banner()
            }
        }
    }


    fun getHomeArticle(pager: Int, call: MutableLiveData<Result<HomeArticle>>) {
        CallResult<HomeArticle>(owner) {
            success { result, message ->
                call.value = result
            }
            outTime {
                call.value = it
            }
            hold {
                api.getHomeArticle(pager)
            }
        }

    }


    fun getChapters(call: MutableLiveData<Result<ArrayList<TabBean>>>) {
        CallResult<ArrayList<TabBean>>(owner){
            success { result, message ->
                call.value = result
            }.hold {
                api.getChapters()
            }
        }
    }


    fun getArticle(id: Int, pager: Int, call: MutableLiveData<Result<Article>>) {
        CallResult<Article>(owner) {
            success { result, message ->
                call.value = result
            }
            outTime {
                call.value = it
            }
            hold {
                api.getArticle(id, pager)
            }
        }

    }


    fun homeCollect(id: Int) {
        CallResult<Empty>(owner) {

            success { result, message ->
                showToast("收藏成功")
            }.error { result, code, message ->
                toast(code, message)
            }.hold {
                api.homeCollect(id)
            }
        }

    }

    fun addCoolect(title: String, author: String, link: String) {
        CallResult<Empty>(owner) {
            success { result, message ->
                showToast("收藏成功")
            }.error { result, code, message ->
                toast(code, message)
            }.hold {
                api.addCoolect(title, author, link)
            }
        }

    }


    fun homeUnCollect(id: Int) {
        CallResult<Empty>(owner) {
            success { result, message ->
                showToast("取消收藏")
            }.error { result, code, message ->
                toast(code, message)
            }.hold {
                api.homeUnCollect(id)
            }

        }

    }

    fun unCollect(id: Int, originId: Int) {
        CallResult<Empty>(owner){
            success { result, message ->
                showToast("取消收藏")
            }.error { result, code, message ->
                toast(code, message)
            }.hold {
                api.unCollect(id, originId)
            }
        }

    }


    fun getTree(call: MutableLiveData<Result<ArrayList<Tree>>>) {
        CallResult<ArrayList<Tree>>(owner) {

            success { result, message ->
                call.value = result
            }.hold {
                api.getTree()
            }
        }

    }


    fun getCateTab(call: MutableLiveData<Result<ArrayList<CateTab>>>) {
        CallResult<ArrayList<CateTab>>(owner){
            success { result, message ->
            call.value = result
        }.hold {
            api.getCateTab()
        }
        }

    }


    fun getCate(pager: Int, id: Int, call: MutableLiveData<Result<CateBean>>) {
        CallResult<CateBean>(owner){
            success { result, message ->
            call.value = result
        }.error { result, code, message ->
            toast(code, message)
        }.hold {
            api.getCate(pager, id)
        }

        }

    }


    fun getCollect(pager: Int, call: MutableLiveData<Collect>) {
        CallResult<Collect>(owner){

            success { result, message ->
            result.apply {
                call.value = response
            }
        }.error { result, code, message ->
            toast(code, message)
        }.hold {
            api.getCollect(pager)
        }

        }

    }


    fun search(key: String, pager: Int, call: MutableLiveData<Result<HomeArticle>>) {
        CallResult<HomeArticle>(owner){
            success { result, message ->
                call.value = result
            }.outTime {
                call.value = it
            }.hold {
                api.search(pager, key)
            }
        }

    }

}