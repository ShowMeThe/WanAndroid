package com.showmethe.wanandroid.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.showmethe.wanandroid.dialog.SignUpDialog
import com.google.android.material.snackbar.Snackbar

import com.showmethe.wanandroid.base.WanApplication
import com.showmethe.wanandroid.constant.ACCOUNT

import com.showmethe.wanandroid.saveAuth
import com.showmethe.wanandroid.ui.auth.vm.AuthViewModel


import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ActivityLoginBinding
import com.showmethe.wanandroid.entity.Login
import showmethe.github.core.base.AppManager
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.http.coroutines.Result.Companion.Success
import showmethe.github.core.util.extras.copyTextToClipboard
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.system.hideSoftKeyboard
import kotlin.random.Random

class LoginActivity : BaseActivity<ActivityLoginBinding, AuthViewModel>() {



    private val bean = Login()

    override fun showCreateReveal(): Boolean = true
    override fun getViewId(): Int = R.layout.activity_login
    override fun initViewModel(): AuthViewModel = createViewModel(AuthViewModel::class.java)

    override fun onBundle(bundle: Bundle) {


    }


    override fun observerUI() {



        
        viewModel.auth.observe(this, Observer {
            it?.apply {
                if (status == Success) {
                    response?.apply {
                        saveAuth(this)
                        WanApplication.lastActivity?.apply {
                            AppManager.get().finishTarget(this)
                            val intent = Intent(context,this)
                            WanApplication.lastBundle?.apply {
                                intent.putExtras(this)
                            }
                            startActivity(intent)
                        }
                        WanApplication.lastActivity = null
                        WanApplication.lastBundle = null
                        finishAfterTransition()
                    }
                }
            }
        })


    }

    override fun init(savedInstanceState: Bundle?) {

        binding?.apply {
            login = this@LoginActivity
            this@LoginActivity.bean.account = RDEN.get(ACCOUNT,"")
            bean = this@LoginActivity.bean
            executePendingBindings()
        }

    }

    override fun initListener() {


    }




    fun login(account:String,password:String){
        router.toTarget("login",account,password)
    }


}
