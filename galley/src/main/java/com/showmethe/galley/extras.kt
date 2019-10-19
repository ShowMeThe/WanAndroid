package com.showmethe.galley

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import showmethe.github.core.glide.*
import showmethe.github.core.glide.TGlide.Companion.loadNoCrop

@BindingAdapter("circle")
fun ImageView.circle(url:String?){
    url?.apply {
        loadCirclePicture(this)
    }
}

@BindingAdapter("noCrop")
fun ImageView.noCrop(url:String?){
    url?.apply {
        loadNoCrop(this)
    }
}


@BindingAdapter("noCropReveal")
fun ImageView.noCropReveal(url:String?){
    url?.apply {
        loadRevealNoCrop(this)
    }
}

@BindingAdapter("crop")
fun ImageView.crop(url:String?){
    url?.apply {
        loadReveal(this)
    }
}