package com.showmethe.wanandroid.ui.auth


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.PermissionChecker
import androidx.databinding.ObservableArrayList

import com.ken.materialwanandroid.ui.auth.vm.AuthViewModel
import com.showmethe.galley.database.DataSourceBuilder
import com.showmethe.galley.database.Source
import com.showmethe.galley.database.dto.GoodsListDto
import com.showmethe.galley.database.dto.PhotoWallDto
import com.showmethe.galley.database.dto.UserDto
import com.showmethe.galley.ui.home.WelcomeActivity


import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.constant.INIT_DATA
import com.showmethe.wanandroid.databinding.ActivitySplashBinding
import com.showmethe.wanandroid.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.extras.double2Decimal
import showmethe.github.core.util.permission.PermissionFactory
import showmethe.github.core.util.permission.RequestPermission
import showmethe.github.core.util.rden.RDEN
import java.security.Permission
import java.util.jar.Manifest
import kotlin.random.Random


class SplashActivity : BaseActivity<ActivitySplashBinding, AuthViewModel>() {

    override fun setTheme() {

    }


    override fun getViewId(): Int = R.layout.activity_splash

    override fun initViewModel(): AuthViewModel = createViewModel(AuthViewModel::class.java)

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }


    @SuppressLint("CheckResult")
    override fun init(savedInstanceState: Bundle?) {

         PermissionFactory.with(this).requestAll()

    }


    @RequestPermission(permissions = [
        android.Manifest.permission.CAMERA])
    private fun checkPermission(result: Boolean){
        initData()
        startToMain()
    }


    private fun startToMain(){
        rect.bindLifecyle(this)
        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            startActivity<MainActivity>(null)
            finishAfterTransition()
        }
    }

    private val source by lazy {  Source.get() }
    private fun initData(){
        if(!RDEN.get(INIT_DATA,false)){
            GlobalScope.launch (Dispatchers.IO) {
                initPhotoWall()
                initGoods()
                RDEN.put(INIT_DATA,true)
            }
        }
    }

    private fun initPhotoWall(){
        val random = Random(System.currentTimeMillis())
        for(i in 0..random.nextInt(15,25)){
            val bean = PhotoWallDto()
            val list = ObservableArrayList<String>()
            for(a in 0..random.nextInt(1,5)){
                list.add(source.getBanner()[(random.nextInt(0,28))])
            }
            bean.id = i
            bean.imageList = list
            bean.avatar = source.getBanner()[(random.nextInt(0,28))]
            bean.userName = source.getUserName()[(random.nextInt(0,13))] + source.getUserName()[(random.nextInt(6,13))]
            bean.isLike = (bean.userName!!.contains("A") or bean.userName!!.contains("e"))
            DataSourceBuilder.getPhotoWall().addPhotoBean(bean)
        }
    }


    private fun initGoods(){
        val random = Random(System.currentTimeMillis())
        for(i in 0..random.nextInt(60,80)){
            val bean = GoodsListDto()
            val list = ObservableArrayList<String>()
            for(a in 0..random.nextInt(3,8)){
                list.add(source.getBanner()[(random.nextInt(0,28))])
            }
            bean.coverImg = list[0]
            bean.goodsImg = list
            bean.goodDes = source.getContent()[random.nextInt(0,2)]
            bean.goodsName = source.getUserName()[(random.nextInt(0,13))] + source.getUserName()[(random.nextInt(6,13))]
            bean.price = double2Decimal(random.nextDouble(10.0, 300.0))
            DataSourceBuilder.getGoods().insertGoods(bean)
        }
    }


    override fun initListener() {



    }


    override fun showFinishReveal(): Boolean = true




}
