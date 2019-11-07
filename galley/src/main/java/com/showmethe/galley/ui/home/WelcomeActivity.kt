package com.showmethe.galley.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.showmethe.galley.R
import com.showmethe.galley.database.Source
import com.showmethe.galley.databinding.ActivityWelcomeBinding
import com.showmethe.galley.entity.LoginBean
import com.showmethe.galley.ui.home.adapter.VerticalAdapter
import com.showmethe.galley.ui.home.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_welcome.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.widget.StatusBarUtil
import showmethe.github.core.util.widget.StatusBarUtil.setFullScreen


class WelcomeActivity : BaseActivity<ActivityWelcomeBinding,MainViewModel>() {


    private val login = LoginBean()
    override fun setTheme() {
       setFullScreen()
    }
    private lateinit var adapter: VerticalAdapter
    private val verList = ObservableArrayList<String>()

    override fun getViewId(): Int = R.layout.activity_welcome
    override fun initViewModel(): MainViewModel = createViewModel(MainViewModel::class.java)

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.user.observe(this, Observer {
            it?.apply {
                dismissLoading()
                if(this.password == login.password){
                    showToast("登录成功")
                    startActivity<GalleyMainActivity>()
                    finishAfterTransition()
                }else{
                    showToast("密码错误")
                }
            }
        })

    }

    override fun init(savedInstanceState: Bundle?) {

        initAdapter()

        login.account = RDEN.get("ACCOUNT","")
        binding?.apply {
            bean = this@WelcomeActivity.login
            login = this@WelcomeActivity
            executePendingBindings()
        }


    }


    override fun initListener() {



    }


    fun login(view: View){
        router.toTarget("getUserByName",login.account)
    }


    private fun initAdapter(){
        rv.bindLife(this)
        verList.addAll(Source.get().getBanner())
        adapter = VerticalAdapter(this,verList)
        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(this,2)
    }

}
