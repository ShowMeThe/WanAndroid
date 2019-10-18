package showmethe.github.core.adapter.slideAdapter

import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter.slideAdapter
 */
class SlideCreator {

    lateinit var slideItems: SlideItem
    private  var menuItemList: ArrayList<MenuItem> = ArrayList()


    fun addItemMenu(content: String, textColor: Int, backgroundColor: Int,textSize:Float = 19f): SlideCreator {
        val item = MenuItem(MenuType.TEXT, content, textSize,textColor, backgroundColor)
        menuItemList.add(item)
        return this
    }

    fun addImageItem(resId: Int, imageBackground: Int): SlideCreator {
        val item = MenuItem(MenuType.IMAGE, resId, imageBackground)
        menuItemList.add(item)
        return this
    }


    /**
     * @param rightMenuRatio 滑动菜单的宽度为全屏幕宽度的百分比
     */
    fun bind(adapter: RecyclerView.Adapter<*>, rightMenuRatio: Float): SlideCreator {
        if (adapter is SlideAdapter<*>) {
            slideItems = SlideItem(rightMenuRatio, menuItemList)
            adapter.setSlideItems(slideItems)
        }
        return this
    }


}
