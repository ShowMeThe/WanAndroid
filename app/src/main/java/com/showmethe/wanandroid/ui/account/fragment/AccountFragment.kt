package com.showmethe.wanandroid.ui.account.fragment




import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout

import com.showmethe.wanandroid.dialog.TextJumpDialog
import com.ken.materialwanandroid.entity.TabItem

import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.FragmentAccuntBinding
import com.showmethe.wanandroid.ui.account.adapter.TabArticleAdapter
import com.showmethe.wanandroid.ui.main.vm.MainViewModel

import kotlinx.android.synthetic.main.fragment_accunt.*
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.util.extras.SimpleTabSelectedListener
import showmethe.github.core.util.widget.StatusBarUtil
import showmethe.github.core.widget.common.SmartRelativeLayout


class AccountFragment : LazyFragment<FragmentAccuntBinding, MainViewModel>() {


    private val dialog = TextJumpDialog()
    private lateinit var adapter : TabArticleAdapter
    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<TabItem>()

    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)
    override fun getViewId(): Int = R.layout.fragment_accunt



    override fun onBundle(bundle: Bundle) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        StatusBarUtil.fixToolbar(context,toolbar)
    }

    override fun observerUI() {

        viewModel.tabs.observe(this, Observer { result ->
            result?.apply {
                response?.apply {
                    forEach {
                        titles.add(TabItem(it.id,it.name))
                        fragments.add(ArticleFragment.get(it.id))
                    }
                    initVp()
                    tab.postDelayed({
                        dialog.dismiss()
                    },1000)
                }
            }
        })
    }


    override fun init() {

        //dialog.show(context.supportFragmentManager,"dialog")
        viewModel.getChapters()




    }

    override fun initListener() {



        tab.addOnTabSelectedListener(object : SimpleTabSelectedListener(){
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.apply {
                    val pos = position

                }
            }
        })

        SmartRelativeLayout.setDefaultLayoutCreator(object : SmartRelativeLayout.DefaultLayoutCreator{
            override fun createLoadingView(): Int = R.layout.smart_loading

            override fun createErrorView(): Int = -1

            override fun createEmptyView(): Int = -1

        })

    }

    private fun initVp(){

        adapter = TabArticleAdapter(fragments,titles,childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vp.adapter = adapter
        vp.offscreenPageLimit = fragments.size
        tab.setupWithViewPager(vp)



    }


}