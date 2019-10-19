package showmethe.github.core.base

import android.app.Activity
import android.app.ActivityOptions
import androidx.lifecycle.*
import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.util.SparseArray
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

import com.jeremyliao.liveeventbus.LiveEventBus
import showmethe.github.core.util.widget.ScreenSizeUtil
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import showmethe.github.core.dialog.DialogLoading
import showmethe.github.core.livebus.LiveBusHelper
import showmethe.github.core.util.toast.ToastFactory
import showmethe.github.core.util.widget.StatusBarUtil
import java.util.concurrent.TimeUnit
import showmethe.github.core.R
import showmethe.github.core.base.vmpath.VMRouter
import showmethe.github.core.util.extras.putValueInBundle
import showmethe.github.core.util.system.hideSoftKeyboard
import kotlin.math.hypot

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
abstract class BaseActivity<V : ViewDataBinding,VM : BaseViewModel> : AppCompatActivity() {

    lateinit var router : VMRouter
    private val loadingDialog  by  lazy {  DialogLoading() }
    var screenWidth = 0
    var screenHeight = 0
    lateinit var context: BaseActivity<*,*>
    var  binding : V? = null
    lateinit var viewModel : VM
    lateinit var root : View
    private val resultMap =  lazy { SparseArray<(((requestCode: Int, resultCode: Int, data: Intent?)->Unit))>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,getViewId())
        setTheme()
        root = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)

        context = this
        binding?.lifecycleOwner = this
        viewModel = initViewModel()
        router = VMRouter(viewModel)
        viewModel.initOwner(this)
        lifecycle.addObserver(viewModel)


        if(showCreateReveal()){
            setUpReveal(savedInstanceState)
        }

        intent.apply {
            extras?.apply {
                onBundle(this)
            }
        }

        AppManager.get().addActivity(this)

        screenWidth = ScreenSizeUtil.getWidth(this)
        screenHeight = ScreenSizeUtil.getHeight(this)


        if (isLiveEventBusHere()) {
            LiveEventBus.get("LiveData",LiveBusHelper::class.java).observe(this,observer)
        }


        observerUI()
        init(savedInstanceState)
        initListener()

    }




    /**
     * 创建Reveal
     */
    private fun setUpReveal(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val viewTreeObserver = root.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        circularReveal(root)
                        root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }
    }



    protected fun addStatusBarView(color: Int, isTxBlack: Boolean) {
        StatusBarUtil.addStatusBarView(this, color, isTxBlack)
    }

    private var observer: Observer<LiveBusHelper> = Observer {
        it?.apply {
            onEventComing(this)
        }
    }



    open fun sendEvent(helper: LiveBusHelper) {
        LiveEventBus.get("LiveData",LiveBusHelper::class.java).post(helper)
    }


    open fun sendEventDelay(helper: LiveBusHelper, delay: Long) {
        LiveEventBus.get("LiveData",LiveBusHelper::class.java).postDelay(helper, delay)
    }


    open fun isLiveEventBusHere(): Boolean {
        return false
    }


    open  fun showCreateReveal() : Boolean{
        return false
    }


    open fun showFinishReveal(): Boolean{
        return false
    }


    open fun onEventComing(helper : LiveBusHelper) {

    }

    fun showLoading(){
        if(!loadingDialog.isAdded){
            loadingDialog.show(supportFragmentManager,"")
        }
    }

    fun dismissLoading(){
        if(loadingDialog.isAdded){
            loadingDialog.dismissAllowingStateLoss()
        }
    }

    fun showToast(message : String){
        ToastFactory.createToast(message)
    }



    fun createViewModel(aClass : Class<VM>) : VM{
        return  ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(application)).get(aClass)
    }


    open fun setTheme(){
    }

    abstract fun getViewId() : Int

    abstract fun initViewModel() : VM

    abstract fun onBundle(bundle: Bundle)

    abstract fun observerUI()

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initListener()


    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        val provider = AndroidLifecycle.createLifecycleProvider(this)
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .compose(provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
        }
    }



    fun <T> applySchedulers(interval: Long ): ObservableTransformer<T, T> {
        val inter = if (interval < 15000) 15000 else interval
        val provider = AndroidLifecycle.createLifecycleProvider(this)
        return ObservableTransformer{
            it.repeatWhen {
                objectObservable ->
                objectObservable.flatMap { Observable.just(1).
                        delay(inter, TimeUnit.MILLISECONDS)
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))
        }
    }


    override fun onBackPressed() {
        finishAfterTransition()
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideSoftKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    // 如果焦点不是EditText则忽略
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.alpha_in,R.anim.alpha_out)
    }

    fun finishReveal(call : ()->Unit){
        if(showFinishReveal()){
            circularFinishReveal(root){
                call.invoke()
            }
        }
    }

    /**
     * startActivity，加入切换动画
     */
    inline fun <reified T>startActivity(bundle: Bundle? = null) {
        val intent = Intent(this, T::class.java)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.alpha_in,R.anim.alpha_out)
    }


    /**
     * 请慎用，部分数据类型不支持
     * 仅作简化操作，支持基本常用的数据类型  不支持ArrayList Array<*>传入
     * 如需正常使用请使用startActivity<*>(bundle: Bundle?)
     * @param  extras 仅支持Int，Byte，Char，Long，Float，Short，Double，Boolean，String等常用类型及部分对应Array类型
     *
     */
    inline fun <reified T >startToActivity(vararg extras: Pair<String, Any>){
        val intent = Intent(this, T::class.java)
        if (extras.isNotEmpty()) {
            val bundle  = Bundle()
            extras.forEach { (key,value) ->
                bundle.putValueInBundle(key, value)
            }
            intent.putExtras(bundle)
        }
       startActivity(intent)
       overridePendingTransition(R.anim.alpha_in,R.anim.alpha_out)
    }


    /**
     * startActivity，加入切换动画
     */
    fun startActivity(bundle: Bundle?, target: Class<*>,vararg  pair: android.util.Pair<View,String>) {
        val intent = Intent(this, target)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (pair.isNotEmpty()) {
            val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this, *pair)
            startActivity(intent, transitionActivityOptions.toBundle())
        }else{
            startActivity(intent)
            overridePendingTransition(R.anim.alpha_in,R.anim.alpha_out)
        }
    }

   fun  startForResult(bundle: Bundle?, requestCode:Int,target: Class<*>,onResult :(((requestCode: Int, resultCode: Int, data: Intent?)->Unit))){
       val intent = Intent(this, target)
       if (bundle != null) {
           intent.putExtras(bundle)
       }
       resultMap.value.append(requestCode,onResult)
       startActivityForResult(intent,requestCode)
       overridePendingTransition(R.anim.alpha_in,R.anim.alpha_out)
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.apply {
            resultMap.value.get(requestCode)?.apply {
                invoke(requestCode, resultCode, data)
                resultMap.value.remove(requestCode)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        AppManager.get().removeActivity(this)
        dismissLoading()
        lifecycle.removeObserver(viewModel)
        LiveEventBus.get("LiveData", LiveBusHelper::class.java).removeObserver(observer)
        if(resultMap.isInitialized()){
            resultMap.value.clear()
        }
    }



    /**
     * 水波纹覆盖显示动画
     * @param rootLayout
     */
    private fun circularReveal(rootLayout: View) {
        val centerX = rootLayout.width.toFloat() /2
        val centerY = rootLayout.height.toFloat() /2
        val finalRadius = hypot(
            centerX.coerceAtLeast(rootLayout.width - centerX),
            centerY.coerceAtLeast(rootLayout.height - centerY)
        )
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            rootLayout,
            (rootLayout.width * 0.5).toInt(),
            (rootLayout.height * 0.5).toInt(),
            0f, finalRadius
        )
        circularReveal.duration = 500
        circularReveal.interpolator = LinearInterpolator()
        circularReveal.startDelay = 50
        circularReveal.start()
    }


    /**
     * 水波纹覆盖关闭消失动画
     * @param rootLayout
     */
    private fun circularFinishReveal(rootLayout: View,call : ()->Unit) {
        val centerX = rootLayout.width.toFloat() /2
        val centerY = rootLayout.height.toFloat() /2
        val finalRadius = hypot(
            centerX.coerceAtLeast(rootLayout.width - centerX),
            centerY.coerceAtLeast(rootLayout.height - centerY)
        )
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            rootLayout,
            (rootLayout.width * 0.5).toInt(),
            (rootLayout.height * 0.5).toInt(), finalRadius,0f
        )
        circularReveal.duration = 500
        circularReveal.interpolator = LinearOutSlowInInterpolator()
        GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            call.invoke()
        }
        circularReveal.start()
    }

}