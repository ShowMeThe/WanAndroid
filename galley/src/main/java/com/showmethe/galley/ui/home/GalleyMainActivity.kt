package com.showmethe.galley.ui.home

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.showmethe.galley.R
import com.showmethe.galley.databinding.ActivityGalleyMainBinding
import com.showmethe.galley.ui.home.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_galley_main.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.widget.StatusBarUtil

class GalleyMainActivity : BaseActivity<ActivityGalleyMainBinding, MainViewModel>() {


    override fun getViewId(): Int  = R.layout.activity_galley_main
    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)
    override fun onBundle(bundle: Bundle) {

    }
    override fun setTheme() {
        StatusBarUtil.fixToolbarScreen(this, toolbar)
    }
    override fun observerUI() {

    }

    override fun init(savedInstanceState: Bundle?) {

        initTab()


    }

    override fun initListener() {

    }


    private fun initTab() {

        tab.addTab(tab.newTab().setText("图片墙").setIcon(ContextCompat.getDrawable(context, R.mipmap.photo)))
        tab.addTab(tab.newTab().setText("购物圈").setIcon(ContextCompat.getDrawable(context, R.mipmap.shopping)))

        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val pos = tab!!.position


            }
        })
    }

}
