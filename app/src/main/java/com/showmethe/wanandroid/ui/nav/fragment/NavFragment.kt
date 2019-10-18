package com.showmethe.wanandroid.ui.nav.fragment


import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.FragmentNavBinding
import com.showmethe.wanandroid.entity.Tree
import com.showmethe.wanandroid.ui.home.SearchActivity
import com.showmethe.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.wanandroid.ui.nav.adapter.TreeAdapter
import kotlinx.android.synthetic.main.fragment_nav.*
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.base.vmpath.VMRouter
import showmethe.github.core.util.widget.StatusBarUtil


class NavFragment : LazyFragment<FragmentNavBinding, MainViewModel>() {


    private val list = ObservableArrayList<Tree>()
    private lateinit var adapter : TreeAdapter
    private val pagerNumber = MutableLiveData<Int>()

    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)
    override fun getViewId(): Int = R.layout.fragment_nav

    override fun onBundle(bundle: Bundle) {

    }


    override fun observerUI() {

        pagerNumber.observe(this, Observer {
            it?.apply {
                router.toTarget("getTree")
            }
        })

        viewModel.tree.observe(this, Observer {
            it?.apply {
                refresh.isRefreshing = false
                list.clear()
                response?.apply {
                    list.addAll(this)
                }

            }
        })


    }

    override fun init() {
        router = VMRouter(viewModel)
        refresh.setColorSchemeResources(R.color.color_6200ea)
        StatusBarUtil.fixToolbar(context,toolbar)

        adapter = TreeAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        rv.hideWhenScrolling(refresh)



        pagerNumber.value = 1

    }

    override fun initListener() {

        refresh.setOnRefreshListener {
            pagerNumber.value = 1
        }


        bar.setNavigationOnClickListener {
            context.startActivity(null, SearchActivity::class.java)
        }

        fab.setOnClickListener {
            rv.smoothScrollToPosition(0)
        }

    }
}