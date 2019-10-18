package com.showmethe.wanandroid

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.showmethe.wanandroid.constant.ACCOUNT
import com.showmethe.wanandroid.constant.HAS_LOGIN
import com.showmethe.wanandroid.constant.USER_ID
import com.ken.materialwanandroid.entity.Auth
import showmethe.github.core.glide.TGlide
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.system.getDateTime
import showmethe.github.core.util.system.yyyyMMdd_HHmm

/**
 * com.ken.materialwanandroid
 *
 * 2019/9/4
 **/

fun saveAuth(auth: Auth){
    RDEN.put(ACCOUNT,auth.username)
    RDEN.put(USER_ID,auth.id)
    RDEN.put(HAS_LOGIN,true)
}


@BindingAdapter("dataTime")
fun TextView.dataTime(time:Long){
    text = getDateTime(time,yyyyMMdd_HHmm)
}

@BindingAdapter("logo")
fun ImageView.logo(url:String?){
    url?.apply {
        TGlide.loadCirclePicture(this,this@logo)
    }
}

@BindingAdapter("logoR")
fun ImageView.logoR(url:String?){
    url?.apply {
        TGlide.loadReveal(this,this@logoR)
    }
}


@BindingAdapter("loadNoCrop")
fun ImageView.loadNoCrop(url:String?){
    url?.apply {
        TGlide.loadNoCrop(this,this@loadNoCrop)
    }
}