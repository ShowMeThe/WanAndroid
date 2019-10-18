package showmethe.github.core.widget.picker

import android.content.Context
import android.widget.TextView

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:51
 * Package Name:showmethe.github.core.widget.picker
 */
class StringWheelAdapter(context: Context, list: ArrayList<String>) : WheelAdapter<String>(context, list) {

    override fun bindItems(textView: TextView, item: String, position: Int) {
        textView.text = item
    }
}