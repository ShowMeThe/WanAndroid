package com.showmethe.wanandroid.ui.auth


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2

import com.showmethe.wanandroid.ui.auth.vm.AuthViewModel
import com.showmethe.galley.database.DataSourceBuilder
import com.showmethe.galley.database.Source
import com.showmethe.galley.database.dto.GoodsListDto
import com.showmethe.galley.database.dto.PhotoWallDto


import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.constant.INIT_DATA
import com.showmethe.wanandroid.databinding.ActivitySplashBinding
import com.showmethe.wanandroid.ui.auth.fragment.InputUserFragment
import com.showmethe.wanandroid.ui.auth.fragment.SplashFragmentAdapter
import com.showmethe.wanandroid.ui.auth.fragment.WelcomeFragment
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.extras.double2Decimal
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.widget.transformer.AlphaScalePageTransformer
import showmethe.github.core.widget.transformer.ParallaxTransformer
import kotlin.random.Random


class SplashActivity : BaseActivity<ActivitySplashBinding, AuthViewModel>() {


    private val list = ArrayList<Fragment>()
    private lateinit var adapter: SplashFragmentAdapter

    override fun setTheme() {

    }

    override fun getViewId(): Int = R.layout.activity_splash

    override fun initViewModel(): AuthViewModel = createViewModel(AuthViewModel::class.java)

    override fun onBundle(bundle: Bundle) {


    }

    override fun observerUI() {

        viewModel.toNext.value = 0
        viewModel.toNext.observe(this, Observer {
            it?.apply {
                vp2.setCurrentItem(this,true)
            }
        })



    }


    @SuppressLint("CheckResult")
    override fun init(savedInstanceState: Bundle?) {

        initAnim()

        list.add(WelcomeFragment())
        list.add(InputUserFragment())

        adapter = SplashFragmentAdapter(list,this)
        vp2.adapter = adapter
        vp2.isUserInputEnabled = false;
        vp2.orientation = ViewPager2.ORIENTATION_VERTICAL
        vp2.offscreenPageLimit = list.size

        initData()

    }


    private fun initAnim(){
        ivLogo.animate()
            .scaleY(1.0f)
            .scaleX(1.0f)
            .alpha(1.0f)
            .setInterpolator(AnticipateOvershootInterpolator())
            .translationY(100f)
            .setDuration(500)
            .setStartDelay(1200)
            .start()
    }


    fun startReg(){
        viewModel.toNext.value = 1
    }

    fun startToLogin(){



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
