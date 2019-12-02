package com.showmethe.galley.ui.home

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.showmethe.galley.R
import com.showmethe.galley.databinding.ActivityImageBinding
import com.showmethe.galley.ui.home.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_image.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.widget.StatusBarUtil
import showmethe.github.core.util.widget.StatusBarUtil.setFullScreen


fun BaseActivity<*,*>.openImage(url:String,transitionView: View){
    val bundle = Bundle()
    bundle.putString("url",url)
    startActivity(bundle,ImageShowActivity::class.java,android.util.Pair(transitionView,"photo"))
}

class ImageShowActivity : BaseActivity<ActivityImageBinding, MainViewModel>() {

    override fun setTheme() {
        setFullScreen()
    }


    private var url = ""

    override fun getViewId(): Int = R.layout.activity_image
    override fun initViewModel(): MainViewModel = createViewModel()

    override fun onBundle(bundle: Bundle) {
        url = bundle.getString("url","")
    }


    override fun observerUI() {

    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.bean = url

        image.setOnExitListener { view, translateX, translateY, w, h ->
           finishAfterTransition()
        }
        image.setOnAlphaListener {
            bg.alpha = it/255
        }

    }

    override fun initListener() {



    }

    override fun finishAfterTransition() {
        super.finishAfterTransition()

    }

}
