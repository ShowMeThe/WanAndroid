package com.showmethe.galley.ui.home.fragment

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showmethe.galley.R
import com.showmethe.galley.database.dto.PhotoWallDto
import com.showmethe.galley.databinding.FragmentPhotoBinding
import com.showmethe.galley.ui.home.ImageShowActivity
import com.showmethe.galley.ui.home.adapter.PhotoAdapter
import com.showmethe.galley.ui.home.openImage
import com.showmethe.galley.ui.home.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_photo.*


import showmethe.github.core.base.BaseFragment
import showmethe.github.core.divider.RecycleViewDivider


class PhotoFragment : BaseFragment<FragmentPhotoBinding, MainViewModel>() {


    val list = ObservableArrayList<PhotoWallDto>()
    lateinit var adapter : PhotoAdapter
    override fun initViewModel(): MainViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_photo

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.bean.observe(this, Observer {
            it?.apply {
                smrl.showContent()
                list.clear()
                list.addAll(this)
            }
        })

    }


    override fun init(savedInstanceState: Bundle?) {


        adapter = PhotoAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        rv.addItemDecoration(RecycleViewDivider(RecyclerView.HORIZONTAL,1,ContextCompat.getColor(context,R.color.color_ff6e00)))


        router.toTarget("getHomePhoto")

    }

    


    override fun initListener() {

        adapter.setOnViewClickListener { view, url ->
            context.openImage(url,view)
        }


        adapter.setOnLikeClickListener { id, like ->
            router.toTarget("setPhotoLike",id,like)
        }

    }
}