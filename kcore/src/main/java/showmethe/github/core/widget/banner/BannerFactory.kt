package showmethe.github.core.widget.banner

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.viewpager2.widget.ViewPager2
import java.lang.ref.WeakReference

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:54
 * Package Name:showmethe.github.core.widget.banner
 */
abstract class BannerFactory(var viewPager2: ViewPager2){

    var delayTime = 0L
    var currentItem  = 0
    var count = 0
    val mHandler = WeakReference(Handler(Looper.getMainLooper())).get()!!

    fun clearTask(task:Runnable){
        mHandler.removeCallbacks(task)
    }

    abstract fun postTask(task:Runnable,time:Long = delayTime)

    abstract fun toNextPage(task:Runnable)

    abstract fun toNextPage(page:Int,smooth:Boolean = true)

    abstract fun onPageSelected(position: Int)

    abstract fun onPageScrollStateChanged(state: Int)

    abstract fun  dispatchTouchEvent(ev: MotionEvent)
}