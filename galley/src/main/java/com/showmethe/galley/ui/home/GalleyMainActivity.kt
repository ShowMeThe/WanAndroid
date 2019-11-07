package com.showmethe.galley.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.tabs.TabLayout
import com.showmethe.galley.R
import com.showmethe.galley.databinding.ActivityGalleyMainBinding
import com.showmethe.galley.ui.home.fragment.PhotoFragment
import com.showmethe.galley.ui.home.fragment.ShoppingFragment
import com.showmethe.galley.ui.home.vm.MainViewModel
import com.showmethe.galley.util.OnBackPressedHandler
import kotlinx.android.synthetic.main.activity_galley_main.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.widget.StatusBarUtil
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbarScreen
import showmethe.github.core.widget.slideback.annotation.SlideBackBinder

@SlideBackBinder
class GalleyMainActivity : BaseActivity<ActivityGalleyMainBinding, MainViewModel>() {


    val shopFragment = PhotoFragment()

    override fun getViewId(): Int  = R.layout.activity_galley_main
    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)
    override fun onBundle(bundle: Bundle) {

    }
    override fun setTheme() {
        fixToolbarScreen( toolbar)
    }
    override fun observerUI() {

    }

    override fun init(savedInstanceState: Bundle?) {


        initTab()

        replaceFragment(PhotoFragment::class.java.name)


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
                switchFragment(pos)
            }
        })
    }


   private fun switchFragment(position: Int) {
        when (position) {
            0 -> {
                replaceFragment(shopFragment::class.java.name)
            }
            1 ->{
                replaceFragment(ShoppingFragment::class.java.name)
            }
        }
    }


    override fun onBackPressed() {
        if(viewModel.handler!=null){
            viewModel.handler?.apply {
                if(!onBackPressed()){
                    super.onBackPressed()
                }
            }
        }else{
            super.onBackPressed()
        }
    }


    private var fragments: List<Fragment>? = null
    private fun replaceFragment(tag: String, id: Int = R.id.frameLayout) {
        var tempFragment = supportFragmentManager.findFragmentByTag(tag)
        val transaction = supportFragmentManager.beginTransaction()
        if (tempFragment == null) {
            try {
                tempFragment = Class.forName(tag).newInstance() as Fragment
                transaction.setCustomAnimations(
                    R.anim.slide_right_in,
                    R.anim.slide_left_out,
                    R.anim.slide_left_in,
                    R.anim.slide_right_out
                )

                transaction.add(id, tempFragment, tag)
                    .setMaxLifecycle(tempFragment, Lifecycle.State.RESUMED)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (i in fragments!!.indices) {
                val fragment = fragments!![i]
                if (fragment.tag == tag) {
                    transaction.setCustomAnimations(
                        R.anim.slide_right_in,
                        R.anim.slide_left_out,
                        R.anim.slide_left_in,
                        R.anim.slide_right_out
                    )
                    transaction.show(fragment)
                } else {
                    transaction.setCustomAnimations(
                        R.anim.slide_right_in,
                        R.anim.slide_left_out,
                        R.anim.slide_left_in,
                        R.anim.slide_right_out
                    )
                    transaction.hide(fragment)
                }
            }
        }
        transaction.commitAllowingStateLoss()
    }

}
