package showmethe.github.core.util.extras

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import showmethe.github.core.glide.TGlide
import showmethe.github.core.widget.common.AutoRecyclerView
import showmethe.github.core.widget.common.SmartRelativeLayout

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.extras
 */


@BindingAdapter("img")
fun ImageView.setImg(url : Any){
    TGlide.load(url,this@setImg)
}


@BindingAdapter("loadMore")
fun AutoRecyclerView.loadMore(loadingMore: (()->Unit)?){
    setOnLoadMoreListener {
        loadingMore?.invoke()
    }
}

@BindingAdapter("onRefresh")
fun SwipeRefreshLayout.onRefresh(onRefreshListener: SwipeRefreshLayout.OnRefreshListener?){
    setOnRefreshListener(onRefreshListener)
}

@BindingAdapter("refreshing")
fun SwipeRefreshLayout.refreshing(newValue: Boolean) {
    if(isRefreshing != newValue)
    isRefreshing = newValue
}

@BindingAdapter("contentState")
fun SmartRelativeLayout.contentState(newValue: Int) {
    when(newValue){
        0 -> showLoading()
        1 -> showEmpty()
        2 -> showError()
        4 -> showContent()
    }
}

inline fun <T : View> T.onGlobalLayout(crossinline  onLayout: T.()->Unit){
    if (viewTreeObserver.isAlive) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                onLayout()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}



