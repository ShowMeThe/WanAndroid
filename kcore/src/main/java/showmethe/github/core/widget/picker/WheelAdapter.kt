package showmethe.github.core.widget.picker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_wheel.view.*
import showmethe.github.core.R


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:51
 * Package Name:showmethe.github.core.widget.picker
 */
 abstract class WheelAdapter<T>(var context: Context, var list:ArrayList<T>) : RecyclerView.Adapter<WheelViewHolder>() {

    private val head = -2058436
    private val footer = -2302569
    private var headOrFooter = 2
    private var itemHeight = 120
    var currentPos = 2
    /**
     * 字体颜色
     */
     var textSelectColor = -1
     var textUnSelectColor = -1

    /**
     * 阴影
     */
     var shadow =  true
     var shadowRadius  = 5f
     var shadowColor  = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WheelViewHolder {
        return if(viewType == footer || viewType == head){
            WheelViewHolder(LinearLayout(parent.context)).setItemLayoutParameter(itemHeight)
        }else{
            WheelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wheel,parent,false)).setItemLayoutParameter(itemHeight)
        }
    }



    override fun getItemCount(): Int = list.size + headOrFooter + headOrFooter

    override fun onBindViewHolder(holder: WheelViewHolder, position: Int) {
        if(getItemViewType(position) != footer && getItemViewType(position)!= head){

            bindItems(holder.itemView.tvWheelTx,list[getRealPos(position)],getRealPos(position))

            if(shadow){
                holder.itemView.tvWheelTx.setShadowLayer(shadowRadius,4f,4f,shadowColor)
            }
            if(currentPos == position){
                holder.itemView.tvWheelTx.textSize = 20f
                holder.itemView.tvWheelTx.setTextColor(textSelectColor)
            }else{
                holder.itemView.tvWheelTx.textSize = 14f
                holder.itemView.tvWheelTx.setTextColor(textUnSelectColor)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        if(position < headOrFooter ){
            return head
        }else if( position > itemCount - 1 - headOrFooter){
            return footer
        }
       return super.getItemViewType(position)
    }

    private fun getRealPos(position : Int) : Int{
        return position - headOrFooter
    }

    fun setPicker( itemHeight: Int, itemMaxShowSize: Int) {
        this.itemHeight = itemHeight
        this.headOrFooter = (itemMaxShowSize - 1) / 2
    }

    abstract fun bindItems(textView: TextView, item: T, position: Int)




}

@FunctionalInterface
interface  onItemTextChange{

    fun onItemChange(textView: TextView, position: Int)

}


class  WheelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


      fun setItemLayoutParameter(height: Int): WheelViewHolder {
        if (itemView.layoutParams == null) {
            itemView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height)
        } else {
            itemView.layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT
            itemView.layoutParams.height = height
        }
        return this
    }
}