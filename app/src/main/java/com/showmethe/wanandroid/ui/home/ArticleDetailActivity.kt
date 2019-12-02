package com.showmethe.wanandroid.ui.home

import android.os.Bundle
import androidx.databinding.ViewDataBinding

import com.showmethe.wanandroid.ui.main.vm.MainViewModel

import com.showmethe.wanandroid.R
import kotlinx.android.synthetic.main.activity_article_detail.*
import showmethe.github.core.base.BaseActivity

fun BaseActivity<*,*>.openDetail(link:String){
    startToActivity<ArticleDetailActivity>("link" to link)
}

class ArticleDetailActivity : BaseActivity<ViewDataBinding, MainViewModel>() {

    private var link = ""

    override fun getViewId(): Int  = R.layout.activity_article_detail

    override fun initViewModel(): MainViewModel = createViewModel()

    override fun onBundle(bundle: Bundle) {
        link = bundle.getString("link","")
    }

    override fun observerUI() {

    }

    override fun init(savedInstanceState: Bundle?) {


        web.defaultSetting()
        web.loadUrl(link)

    }

    override fun initListener() {

        titleView.setOnNavigationClickListener {
            finishAfterTransition()
        }



    }


}
