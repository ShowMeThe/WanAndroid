package showmethe.github.core.widget.picker

import android.content.Context
import android.widget.TextView

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:50
 * Package Name:showmethe.github.core.widget.picker
 */
class IntegerWheelAdapter(context: Context, list: ArrayList<Int>) : WheelAdapter<Int>(context, list) {
    override fun bindItems(textView: TextView, item: Int, position: Int) {
        textView.text = "$item"
    }
}