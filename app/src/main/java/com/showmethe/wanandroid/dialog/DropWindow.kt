package com.showmethe.wanandroid.dialog

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showmethe.wanandroid.dialog.adapter.DropAdapter

import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.entity.CateTab
import kotlinx.android.synthetic.main.window_drop.view.*
import showmethe.github.core.dialog.SimplePopWindow
import showmethe.github.core.divider.RecycleViewDivider

/**
 * com.ken.materialwanandroid.dialog
 *
 * 2019/9/9
 **/
class DropWindow(var context: Context) :
    SimplePopWindow(context) {

    private lateinit var adapter: DropAdapter
    private lateinit var  list :ObservableArrayList<CateTab>

    override fun build(): Int {
        buildDialog {
            R.layout.window_drop
        }.onView {
            list = ObservableArrayList()
            it.apply {
                adapter = DropAdapter(context,list)
                rv.adapter  = adapter
                rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
                rv.addItemDecoration(RecycleViewDivider(RecyclerView.VERTICAL,1,ContextCompat.getColor(context,R.color.color_ff8a80)))

                adapter.setOnItemClickListener { view, position ->
                    val item = list[position]
                    onItemClick?.invoke(item.id,item.name)
                }
            }
        }
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    fun addList(data: ArrayList<CateTab>){
        list.clear()
        list.addAll(data)
    }

    private var  onItemClick :((id:Int,name:String)->Unit)? = null
    fun setOnItemClickListener(onItemClick :((id:Int,name:String)->Unit)){
        this.onItemClick = onItemClick
    }

}