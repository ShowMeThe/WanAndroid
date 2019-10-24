package com.showmethe.wanandroid.ui.main

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.viewpager2.widget.ViewPager2
import com.showmethe.wanandroid.R


import com.showmethe.wanandroid.constant.onPage
import com.showmethe.wanandroid.constant.onStartBanner
import com.showmethe.wanandroid.constant.onStopBanner
import com.showmethe.wanandroid.ui.main.adapter.ImageViewAdapter
import com.showmethe.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_image_show.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.livebus.LiveBusHelper
import showmethe.github.core.util.widget.StatusBarUtil

fun BaseActivity<*,*>.startToImg(url:String,transitionView: View){
    val bundle = Bundle()
    bundle.putString("url",url)
    bundle.putInt("type",1)
    startActivity(bundle,ImageShowActivity::class.java,android.util.Pair(transitionView,"photo"))
}

fun BaseActivity<*,*>.startToImgs(url:ArrayList<String>,position: Int,transitionView: View){
    val bundle = Bundle()
    bundle.putStringArrayList("url",url)
    bundle.putInt("type",2)
    bundle.putInt("position",position)
    startActivity(bundle,ImageShowActivity::class.java,android.util.Pair(transitionView,"photo"))
}



class ImageShowActivity : BaseActivity<ViewDataBinding, MainViewModel>() {

    override fun setTheme() {
        StatusBarUtil.setFullScreen(this)
    }

    private var url = ""
    private var position = 0
    private var type = 1

    private lateinit var adapter: ImageViewAdapter
    private val list = ObservableArrayList<String>()

    override fun getViewId(): Int = R.layout.activity_image_show
    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)

    override fun onBundle(bundle: Bundle) {
        type = bundle.getInt("type",1)
        when(type){
            1 ->  {
                url = bundle.getString("url","")
                list.add(url)
            }
            2 -> {
                list.addAll(bundle.getStringArrayList("url")!!)
                position = bundle.getInt("position",0)
            }
        }

    }


    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {

        if(type == 2){
            sendEvent(LiveBusHelper(onStopBanner))
        }

        adapter = ImageViewAdapter(context,list)
        vp2.adapter = adapter
        vp2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vp2.setCurrentItem(position,false)

    }

    override fun initListener() {

        adapter.setOnFinishListener {
            finishAfterTransition()
        }

        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if(type == 2){
                    sendEvent(LiveBusHelper(onPage,position))
                }

            }
        })

    }

    override fun finishAfterTransition() {
        super.finishAfterTransition()
        if(type == 2){
            sendEvent(LiveBusHelper(onStartBanner))
        }
    }

}
