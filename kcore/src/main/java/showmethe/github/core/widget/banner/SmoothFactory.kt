package showmethe.github.core.widget.banner

import android.view.MotionEvent
import androidx.viewpager2.widget.ViewPager2

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:54
 * Package Name:showmethe.github.core.widget.banner
 */
class SmoothFactory(viewPager2: ViewPager2) : BannerFactory(viewPager2) {



    private var downX = 0f

    override fun toNextPage(task:Runnable) {
        val current = currentItem % count
        viewPager2.setCurrentItem(current,true)
        currentItem++
        postTask(task)
    }

    override fun postTask(task: Runnable, time: Long) {
        mHandler.postDelayed(task,time)
    }


    override fun toNextPage(page: Int, smooth: Boolean) {
        viewPager2.setCurrentItem(page,smooth)
    }


    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {


    }

    override fun dispatchTouchEvent(ev: MotionEvent) {
        when(ev.action){
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL,MotionEvent.ACTION_OUTSIDE -> {
                val current = (currentItem % (count))
                if(downX - ev.x < 0 && current == 0){
                    viewPager2.post {
                        toNextPage(count - 1)
                    }
                }else if(downX - ev.x > 0 && current == count - 1){
                    viewPager2.post {
                        toNextPage(0)
                        currentItem++
                    }
                }
            }
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                currentItem = viewPager2.currentItem
            }
        }
    }
}