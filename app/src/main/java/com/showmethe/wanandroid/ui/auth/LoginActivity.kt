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
import com.ken.materialwanandroid.ui.auth.vm.AuthViewModel


import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ActivityLoginBinding
import com.showmethe.wanandroid.entity.Login
import kotlinx.android.synthetic.main.activity_login.*
import showmethe.github.core.base.AppManager
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.http.coroutines.Result.Companion.Success
import showmethe.github.core.util.extras.copyTextToClipboard
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.system.hideSoftKeyboard
import kotlin.random.Random

class LoginActivity : BaseActivity<ActivityLoginBinding, AuthViewModel>() {

    val dialog by lazy { SignUpDialog() }

    private val bean = Login()
    private val random = Random(System.currentTimeMillis())
    private var snackbar: Snackbar? = null
    private var num = 0

    override fun showCreateReveal(): Boolean = true
    override fun getViewId(): Int = R.layout.activity_login
    override fun initViewModel(): AuthViewModel = createViewModel(AuthViewModel::class.java)

    override fun onBundle(bundle: Bundle) {
    }


    override fun observerUI() {

        viewModel.register.observe(this, Observer {
            it?.apply {
                if (status == Success) {
                    showToast("注册成功")
                    dialog.dismiss()
                }
            }
        })
        
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
        initAnim()
        binding?.apply {
            login = this@LoginActivity
            this@LoginActivity.bean.account = RDEN.get(ACCOUNT,"")
            bean = this@LoginActivity.bean
            executePendingBindings()
        }

    }

    override fun initListener() {



        dialog.setOnCodeGetListener {
            showSnack(it)
        }


        dialog.setOnRegisterGetListener {
            if (it.code.toInt() == num) {
                viewModel.register(it.account, it.password)
            } else {
                showToast("请输入正确的验证码")
            }
        }


    }


    fun showReg(){
        dialog.show(supportFragmentManager, "signUp")
    }


    fun login(account:String,password:String){
        viewModel.login(account,password)
    }


    fun showSnack(view: TextView) {
        hideSoftKeyboard()
        num = random.nextInt(200, 9999)
        snackbar = Snackbar.make(view, "${num}", 15000)
            .setBackgroundTint(ContextCompat.getColor(context, R.color.colorPrimary))
            .setTextColor(ContextCompat.getColor(context, R.color.white))
            .setActionTextColor(ContextCompat.getColor(context, R.color.white))
            .setAction("复制") {
                copyTextToClipboard(context, "${num}")
                showToast("复制成功")
                snackbar!!.dismiss()
            }
        snackbar!!.show()
    }


    private fun initAnim() {
        rect.stopAnim()
        rect.postDelayed({
            rect.background = null
            rect.startAnim()
        }, 400)
    }

}
