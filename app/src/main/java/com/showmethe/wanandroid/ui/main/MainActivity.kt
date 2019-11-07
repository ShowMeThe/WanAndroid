package com.showmethe.wanandroid.ui.main

import android.animation.Animator
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import androidx.viewpager.widget.ViewPager
import com.showmethe.galley.database.DataSourceBuilder
import com.showmethe.galley.ui.home.GalleyMainActivity
import com.showmethe.galley.ui.home.WelcomeActivity

import com.showmethe.wanandroid.R

import com.showmethe.wanandroid.constant.HAS_LOGIN
import com.showmethe.wanandroid.databinding.ActivityMainBinding
import com.showmethe.wanandroid.offline
import com.showmethe.wanandroid.ui.account.fragment.AccountFragment
import com.showmethe.wanandroid.ui.auth.SplashActivity
import com.showmethe.wanandroid.ui.home.fragment.HomeFragment
import com.showmethe.wanandroid.ui.main.adapter.MainAdapter
import com.showmethe.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.wanandroid.ui.nav.fragment.NavFragment
import com.showmethe.wanandroid.ui.project.fragment.ProjectFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import showmethe.github.core.base.AppManager
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.glide.TGlide
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.util.extras.SimpleAnimatorListener
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.widget.StatusBarUtil
import showmethe.github.core.util.widget.StatusBarUtil.setFullScreen

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private val colors = ArrayList<ColorStateList>()

    override fun setTheme() {
       setFullScreen()
    }

    private val fragments = ArrayList<Fragment>()
    private lateinit var adapter: MainAdapter

    override fun showCreateReveal(): Boolean = true
    override fun getViewId(): Int = R.layout.activity_main
    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)

    override fun onBundle(bundle: Bundle) {

    }



    override fun observerUI() {

        viewModel.openDrawer.observe(this, Observer {
            it?.apply {
                if(this){
                    drawer.openDrawer(GravityCompat.START)
                }
            }
        })


    }


    override fun onResume() {
        super.onResume()
        checkLogin()
    }



    override fun init(savedInstanceState: Bundle?) {
        preload()

        colors.add(resources.getColorStateList(R.color.tab_color,null))
        colors.add(resources.getColorStateList(R.color.tab_color_1,null))
        colors.add(resources.getColorStateList(R.color.tab_color_2,null))
        colors.add(resources.getColorStateList(R.color.tab_color_3,null))
        setColorState(0)

        fragments.add(HomeFragment())
        fragments.add(AccountFragment())
        fragments.add(NavFragment())
        fragments.add(ProjectFragment())


        adapter = MainAdapter(fragments,supportFragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 4

        /**
         * GoToArticle 时候跳到第二页
         */
        if(intent.action == "GoToArticle"){
            bottomView.menu.getItem(1).isChecked = true
            setColorState(1)
            viewPager.setCurrentItem(1,true)
        }



    }

    override fun initListener() {

        bottomView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.tabHome ->{
                    setColorState(0)
                    viewPager.setCurrentItem(0,true)
                }

                R.id.tabArea ->{
                    setColorState(1)
                    viewPager.setCurrentItem(1,true)
                }

                R.id.tabNav ->{
                    setColorState(2)
                    viewPager.setCurrentItem(2,true)
                }

                R.id.tabPro ->{
                    setColorState(3)
                    viewPager.setCurrentItem(3,true)
                }
            }
            false
        }


        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                bottomView.menu.getItem(position).isChecked = true
                setColorState(position)
            }
        })

        tvLogout.setOnClickListener {
            if(RDEN.get(HAS_LOGIN,false)){
                startOutReveal()
                viewModel.logout()
            }
        }

        tvLogin.setOnClickListener {
            offline()
        }

        tvCollect.setOnClickListener {
            startActivity<CollectActivity>(null)
        }

        tvGalley.setOnClickListener {
             if(RDEN.get(HAS_LOGIN,false)){
                 startActivity<GalleyMainActivity>()
             }else{
                 startActivity<WelcomeActivity>()
             }
        }

    }




    private fun startOutReveal(){
       val animator =  ViewAnimationUtils.createCircularReveal(topBg,topBg.width/2,topBg.height/2,0f,
            topBg.height.toFloat()
        ).setDuration(800)
        animator.addListener(object : SimpleAnimatorListener() {
            override fun onAnimationEnd(p0: Animator?) {
            }
            override fun onAnimationStart(p0: Animator?) {
                RDEN.put(HAS_LOGIN,false)
                RDEN.put("sessionId","")
                RetroHttp.get().headerInterceptor.update("")
            }
        })
        GlobalScope.launch (Dispatchers.Main){
            delay(600)
            checkLogin()
        }
        animator.start()
    }


    private fun checkLogin(){
        if(RDEN.get(HAS_LOGIN,false)){
            ivHead.visibility = View.VISIBLE
            tvLogin.visibility  = View.INVISIBLE
        }else{
            tvLogin.visibility = View.VISIBLE
            ivHead.visibility  = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }


    private fun setColorState(position: Int){
        bottomView.itemIconTintList = colors[position]
        bottomView.itemTextColor = bottomView.itemIconTintList
        topBg.backgroundTintList =  bottomView.itemTextColor
        tvLogout.setTextColor(bottomView.itemTextColor)
        tvLogout.compoundDrawableTintList = bottomView.itemTextColor
        tvCollect.compoundDrawableTintList = bottomView.itemTextColor
        tvCollect.setTextColor( bottomView.itemTextColor)
        tvGalley.compoundDrawableTintList = bottomView.itemTextColor
        tvGalley.setTextColor(bottomView.itemTextColor)
    }

    /**
     * 预加载部分图片颜色信息
     */
    private fun preload(){
        val defaultColor = ContextCompat.getColor(context, com.showmethe.galley.R.color.color_ff6e00)
        GlobalScope.launch (Dispatchers.IO){
            val goodsList = DataSourceBuilder.getGoods().findAllGoods();
            for(goods in goodsList){
                if(goods.vibrantColor!=-1){
                    TGlide.loadIntoBitmap(goods.coverImg){ bitmap ->
                        Palette.from(bitmap).generate {
                            it?.apply {
                                goods.vibrantColor = getVibrantColor(defaultColor)
                            }
                        }
                    }
                }
            }
        }
    }

}
