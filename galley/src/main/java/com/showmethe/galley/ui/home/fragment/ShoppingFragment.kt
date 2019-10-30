package com.showmethe.galley.ui.home.fragment

import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.showmethe.galley.R
import com.showmethe.galley.database.dto.GoodsListDto
import com.showmethe.galley.databinding.FragmentShoppingBinding
import com.showmethe.galley.ui.home.adapter.GoodsAdapter
import com.showmethe.galley.ui.home.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_shopping.*
import showmethe.github.core.adapter.AutoLoadAdapter
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.base.LazyFragment

/**
 * Author: showMeThe
 * Update Time: 2019/10/19
 * Package Name:com.showmethe.galley.ui.home.fragment
 */
class ShoppingFragment : BaseFragment<FragmentShoppingBinding, MainViewModel>() {



    val refreshing  = MutableLiveData<Boolean>()
    private val pageNumber = MutableLiveData<Int>()
    private lateinit var adapter : GoodsAdapter
    private val list = ObservableArrayList<GoodsListDto>()

    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)

    override fun getViewId(): Int = R.layout.fragment_shopping

    override fun onBundle(bundle: Bundle) {


    }

    override fun observerUI() {

        pageNumber.observe(this, Observer {
            it?.apply {
                router.toTarget("findByPage",it)
            }
        })

        viewModel.goods.observe(this, Observer {
            it?.apply {
                if(pageNumber.value == 1){
                    list.clear()
                }
                list.addAll(this)
                onLoadSize(size)
            }
        })


    }

    override fun init(savedInstanceState: Bundle?) {
        refresh.setColorSchemeResources(R.color.colorAccent)
        adapter  = GoodsAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(context,2)

        binding?.shopping = this

        pageNumber.value  = 1
    }

    fun onRefresh(){
        pageNumber.value  = 1
    }


    override fun initListener() {


        adapter.setOnLoadMoreListener {
            pageNumber.value  = pageNumber.value!! + 1
        }


        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState == SCROLL_STATE_IDLE ){
                    fab.extend()
                }else {
                    fab.shrink()
                }
            }
        })

    }

    private  fun onLoadSize(size:Int){
        refreshing.value  = false
        if(size<10){
            adapter.setEnableLoadMore(false)
        }else{
            adapter.setEnableLoadMore(true)
        }
    }

}