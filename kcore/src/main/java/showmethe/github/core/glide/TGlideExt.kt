package showmethe.github.core.glide

import android.widget.ImageView


fun ImageView.load(url: Any){
    TGlide.load(url,this)
}

fun ImageView.load(url: Any,placeholder: Int, error: Int){
    TGlide.load(url,placeholder,error,this)
}

fun ImageView.loadReveal(url: Any){
    TGlide.loadReveal(url,this)
}

fun ImageView.loadScale(url: Any){
    TGlide.loadScale(url,this)
}

fun ImageView.loadNoCrop(url: Any){
    TGlide.loadNoCrop(url,this)
}

fun ImageView.loadRevealNoCrop(url: Any){
    TGlide.loadRevealNoCrop(url,this)
}

fun ImageView.loadScaleNoCrop(url: Any){
    TGlide.loadScaleNoCrop(url,this)
}

fun ImageView.loadInBackground(url: Any){
    TGlide.loadInBackground(url,this)
}

fun ImageView.loadCirclePicture(url: Any){
    TGlide.loadCirclePicture(url,this)
}

fun ImageView.loadCirclePicture(url: Any,placeholder: Int, error: Int){
    TGlide.loadCirclePicture(url,placeholder,error,this)
}

fun ImageView.loadRoundPicture(url: Any,radius: Int){
    TGlide.loadRoundPicture(url,this,radius)
}


fun ImageView.loadCutPicture(url: Any,radius: Int){
    TGlide.loadCutPicture(url,this,radius)
}

fun ImageView.loadBlurPicture(url: Any,radius: Int){
    TGlide.loadBlurPicture(url,this,radius)
}