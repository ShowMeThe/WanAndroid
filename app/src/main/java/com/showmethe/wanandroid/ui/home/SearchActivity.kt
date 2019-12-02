package com.showmethe.wanandroid.ui.home


import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ken.materialwanandroid.ui.home.vm.SearchViewModel
import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ActivitySearchBinding
import com.showmethe.wanandroid.entity.HomeArticle
import com.showmethe.wanandroid.entity.Search
import com.showmethe.wanandroid.ui.home.adapter.SearchAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.include_main_title.*
import kotlinx.android.synthetic.main.item_search_bar.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.http.coroutines.Result

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {



    private var keyWord = Search()


    private val list = ObservableArrayList<HomeArticle.DatasBean>()
    lateinit var adapter: SearchAdapter
    private val pagerNumber = MutableLiveData<Int>()

    override fun getViewId(): Int = R.layout.activity_search

    override fun initViewModel(): SearchViewModel = createViewModel()

    override fun onBundle(bundle: Bundle) {


    }



    override fun observerUI() {

        binding?.apply {
            search = keyWord
            executePendingBindings()

        }

        pagerNumber.observe(this, Observer {
            it?.apply {
                router.toTarget("search",keyWord.keyWord,this)
            }
        })

        viewModel.search.observe(this, Observer {
            it?.apply {
                refresh.isRefreshing = false
                when(status){
                    Result.Success ->{
                        response?.apply {
                            if(pagerNumber.value!! == 1){
                                list.clear()
                            }
                            list.addAll(datas)
                            if(list.size == 0){
                                smrl.showEmpty()
                            }else{
                                smrl.showContent()
                            }
                            onSize(datas.size)
                        }
                    }
                    Result.OutTime ->{
                        adapter.loadingOverTime()
                    }
                }

            }
        })


    }

    override fun init(savedInstanceState: Bundle?) {
        refresh.setColorSchemeResources(R.color.colorAccent)

        adapter = SearchAdapter(this,list)
        rvHis.adapter = adapter
        rvHis.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)



    }

    override fun initListener() {

        adapter.setOnLoadMoreListener {
            pagerNumber.value = pagerNumber.value!! + 1
        }

        refresh.setOnRefreshListener {
            pagerNumber.value = 0
        }


        edSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                 pagerNumber.value = 0
                true
            }else{
                false
            }
        }

        adapter.setOnItemClickListener { view, position ->
            context.openDetail(list[position].link)
        }

        smrl.setOnReloadWhenErrorOrEmpty {
            pagerNumber.value = 0
        }

    }


    private fun onSize(size:Int){
        if(size == 0){
            adapter.setEnableLoadMore(false)
        }else{
            adapter.setEnableLoadMore(true)
        }
    }



}
