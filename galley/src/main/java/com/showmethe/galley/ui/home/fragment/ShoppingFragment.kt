package com.showmethe.galley.ui.home.fragment

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.showmethe.galley.R
import com.showmethe.galley.database.dto.CartDto
import com.showmethe.galley.database.dto.GoodsListDto
import com.showmethe.galley.databinding.FragmentShoppingBinding
import com.showmethe.galley.entity.CartListBean
import com.showmethe.galley.ui.home.adapter.CartListAdapter
import com.showmethe.galley.ui.home.adapter.GoodsAdapter
import com.showmethe.galley.ui.home.vm.MainViewModel
import com.showmethe.galley.util.OnBackPressedHandler
import kotlinx.android.synthetic.main.fragment_shopping.*
import showmethe.github.core.adapter.AutoLoadAdapter
import showmethe.github.core.adapter.SpaceItemDecoration
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.base.LazyFragment

/**
 * Author: showMeThe
 * Update Time: 2019/10/19
 * Package Name:com.showmethe.galley.ui.home.fragment
 */
class ShoppingFragment : BaseFragment<FragmentShoppingBinding, MainViewModel>(),OnBackPressedHandler {



    val refreshing  = MutableLiveData<Boolean>()
    private val pageNumber = MutableLiveData<Int>()

    private lateinit var adapter : GoodsAdapter
    private val list = ObservableArrayList<GoodsListDto>()

    private lateinit var cartAdapter : CartListAdapter
    private val cart = ObservableArrayList<CartListBean>()

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

        viewModel.carts.observe(this, Observer {
            it?.apply {
                cart.clear()
                cart.addAll(this)
            }
        })


    }

    override fun init(savedInstanceState: Bundle?) {
        viewModel.handler = this
        refresh.setColorSchemeResources(R.color.colorAccent)
        adapter  = GoodsAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(context,2)

        cartAdapter = CartListAdapter(context,cart)
        rvCart.adapter = cartAdapter
        rvCart.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        rvCart.addItemDecoration(SpaceItemDecoration(0,30))

        binding?.shopping = this

        pageNumber.value  = 1

        router.toTarget("getCartList")
    }

    fun onRefresh(){
        pageNumber.value  = 1
    }


    override fun initListener() {


        adapter.setOnLoadMoreListener {
            pageNumber.value  = pageNumber.value!! + 1
        }

        adapter.setOnBuyClickListener {
            val dto = CartDto()
            dto.goodsId = it
            router.toTarget("addCart",dto)
        }

        fab.setOnClickListener {
            fab.isExpanded = true
            fab.hide()
        }


    }

    private  fun onLoadSize(size:Int){
        refreshing.value  = false
        if(size<10){
            adapter.setEnableLoadMore(false)
        }else{
            adapter.setEnableLoadMore(true)
        }
    }

    override fun onBackPressed(): Boolean {
        if(fab.isEnabled){
            fab.isExpanded = false
            fab.show()
           return true
        }
        return false
    }


}