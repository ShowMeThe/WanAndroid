package com.showmethe.wanandroid.ui.auth


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.showmethe.galley.database.DataSourceBuilder
import com.showmethe.galley.database.Source
import com.showmethe.galley.database.dto.GoodsListDto
import com.showmethe.galley.database.dto.PhotoWallDto
import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.base.WanApplication
import com.showmethe.wanandroid.constant.INIT_DATA
import com.showmethe.wanandroid.databinding.ActivitySplashBinding
import com.showmethe.wanandroid.saveAuth
import com.showmethe.wanandroid.ui.auth.fragment.InputPswFragment
import com.showmethe.wanandroid.ui.auth.fragment.InputUserFragment
import com.showmethe.wanandroid.ui.auth.fragment.SplashFragmentAdapter
import com.showmethe.wanandroid.ui.auth.fragment.WelcomeFragment
import com.showmethe.wanandroid.ui.auth.vm.AuthViewModel
import com.showmethe.wanandroid.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import showmethe.github.core.base.AppManager
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.util.extras.double2Decimal
import showmethe.github.core.util.rden.RDEN
import kotlin.random.Random


class SplashActivity : BaseActivity<ActivitySplashBinding, AuthViewModel>() {


    private val list = ArrayList<Fragment>()
    private lateinit var adapter: SplashFragmentAdapter
    private val point = PointF()
    private var isLeft = false

    override fun setTheme() {

    }

    override fun getViewId(): Int = R.layout.activity_splash

    override fun initViewModel(): AuthViewModel = createViewModel(AuthViewModel::class.java)

    override fun onBundle(bundle: Bundle) {


    }

    override fun observerUI() {

        viewModel.toNext.value = -1
        viewModel.toNext.observe(this, Observer {
            it?.apply {
                if(this == 0){
                    reverse()
                    isLeft = false
                }else if(!isLeft && this>0){
                    toLeft()
                    isLeft = true
                }
                if(this >-1){
                    vp2.setCurrentItem(this,true)
                }
            }
        })

        viewModel.register.observe(this, Observer {
            it?.apply {
                if (status == Result.Success) {
                    showToast("注册成功,稍后自动登录")
                    viewModel.registerBean.apply {
                        router.toTarget("login",account,password)
                    }
                }
            }
        })



        viewModel.auth.observe(this, Observer {
            it?.apply {
                if (status == Result.Success) {
                    response?.apply {
                        saveAuth(this)
                        WanApplication.lastActivity?.apply {
                            AppManager.get().finishTarget(this)
                            val intent = Intent(context,this)
                            WanApplication.lastBundle?.apply {
                                intent.putExtras(this)
                            }
                            startActivity(intent)
                        }
                        WanApplication.lastActivity = null
                        WanApplication.lastBundle = null
                        finishAfterTransition()
                    }
                }
            }
        })

    }


    @SuppressLint("CheckResult")
    override fun init(savedInstanceState: Bundle?) {

        initAnim()

        list.add(WelcomeFragment())
        list.add(InputUserFragment())
        list.add(InputPswFragment())

        adapter = SplashFragmentAdapter(list,this)
        vp2.adapter = adapter
        vp2.isUserInputEnabled = false
        vp2.orientation = ViewPager2.ORIENTATION_VERTICAL
        vp2.offscreenPageLimit = 3

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



    private fun toLeft(){
        val dx = ivLogo.x - 15
        point.set(ivLogo.x,ivLogo.y)
        ivLogo
            .animate()
            .scaleY(0.7f)
            .scaleX(0.7f)
            .setInterpolator(LinearInterpolator())
            .translationY(-50f)
            .translationXBy(-dx)
            .setDuration(500)
            .setStartDelay(0)
            .start()
    }

    private fun reverse(){
        ivLogo
            .animate()
            .scaleY(1.0f)
            .scaleX(1.0f)
            .setInterpolator(LinearInterpolator())
            .translationXBy(point.x - 15)
            .translationY(50f)
            .setDuration(500)
            .setStartDelay(0)
            .start()
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
