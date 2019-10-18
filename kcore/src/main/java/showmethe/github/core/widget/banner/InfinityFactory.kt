package showmethe.github.core.widget.banner

import android.view.MotionEvent
import androidx.viewpager2.widget.ViewPager2

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:54
 * Package Name:showmethe.github.core.widget.banner
 */
class InfinityFactory(viewPager2: ViewPager2) : BannerFactory(viewPager2) {


    override fun postTask(task: Runnable, time: Long) {
        mHandler.postDelayed(task,time)
    }

    override fun toNextPage(task: Runnable) {
        currentItem = currentItem % (count + 1) + 1
        if (currentItem == 1) run {
            toNextPage(currentItem,false)
            mHandler.post(task)
        }else{
            toNextPage(currentItem,true)
            mHandler.postDelayed(task, delayTime.toLong())
        }
    }

    override fun toNextPage(page: Int, smooth: Boolean) {
        viewPager2.setCurrentItem(page,smooth)
    }

    override fun onPageSelected(position: Int) {
        currentItem = position
    }

    override fun onPageScrollStateChanged(state: Int) {
        when(state){
            ViewPager2.SCROLL_STATE_IDLE -> {
                if(currentItem == count - 1){
                    toNextPage(1,false)
                }else if(currentItem == 0){
                    toNextPage(count-2,false)
                }
            }
            else ->{}
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent) {
    }
}