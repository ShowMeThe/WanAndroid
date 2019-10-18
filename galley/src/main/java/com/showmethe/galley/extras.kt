package com.showmethe.galley

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import showmethe.github.core.glide.TGlide
import showmethe.github.core.glide.TGlide.Companion.loadNoCrop

@BindingAdapter("circle")
fun ImageView.circle(url:String?){
    url?.apply {
        TGlide.loadCirclePicture(this,this@circle);
    }
}

@BindingAdapter("noCrop")
fun ImageView.noCrop(url:String?){
    url?.apply {
        loadNoCrop(this,this@noCrop)
    }
}