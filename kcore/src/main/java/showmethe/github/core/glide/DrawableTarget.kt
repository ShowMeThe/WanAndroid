package showmethe.github.core.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.util.Util


/**
 * showmethe.github.core.glide
 *
 * 2019/1/10
 **/
abstract class DrawableTarget constructor(private  val width: Int  = SIZE_ORIGINAL
                                          ,private  val height: Int = SIZE_ORIGINAL) : Target<Drawable>{

    override fun onLoadStarted(placeholder: Drawable?) {

    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
    }

    override fun getSize(cb: SizeReadyCallback) {
        if (!Util.isValidDimensions(width, height)) {
            throw IllegalArgumentException(
        "Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given" + " width: "
        + width + " and height: " + height + ", either provide dimensions in the constructor"
        + " or call override()")
        }
        cb.onSizeReady(width, height)
    }

    override fun getRequest(): Request?  = null

    override fun onStop() {
    }

    override fun setRequest(request: Request?) {
    }

    override fun removeCallback(cb: SizeReadyCallback) {
    }

    override fun onLoadCleared(placeholder: Drawable?) {
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        resourceReady(resource,transition)
    }

    override fun onStart() {
    }

    override fun onDestroy() {

    }

    abstract fun resourceReady(resource: Drawable,transition: Transition<in Drawable>?)
}