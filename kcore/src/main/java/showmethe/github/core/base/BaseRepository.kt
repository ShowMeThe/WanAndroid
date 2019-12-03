package showmethe.github.core.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import showmethe.github.core.base.vmpath.VMRouter
import showmethe.github.core.util.toast.ToastFactory
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
open class BaseRepository :  DefaultLifecycleObserver {

    private  var refresh : WeakReference<SwipeRefreshLayout>? = null
    lateinit var owner : LifecycleOwner
    private lateinit var router: VMRouter


    override fun onDestroy(owner: LifecycleOwner) {
        onClear()
    }

    fun init(router: VMRouter) : BaseRepository{
        this.owner = router.owner
        this.router.owner.lifecycle.addObserver(this)
        return this
    }


    open fun initRefresh(refresh : SwipeRefreshLayout) : BaseRepository{
        if(this.refresh == null){
            this.refresh = WeakReference(refresh)
        }
        return this
    }


    fun showRefresh(isLoading : Boolean){
        refresh?.apply {
            get()?.apply {
                isRefreshing = isLoading
            }
        }
    }


    fun showLoading(){
        ContextProvider.get().getActivity().apply {
            if(this is BaseActivity<*,*>){
                this.showLoading()
            }
        }
    }

    fun dismissLoading(){
        ContextProvider.get().getActivity().apply {
            if(this is BaseActivity<*,*>){
                this.dismissLoading()
            }
        }
    }


        fun showToast(message: String) {
            ToastFactory.createToast(message)
        }


        /**
         * 适当使用避免造成内存泄漏
         */
        private fun onClear() {
            if(refresh!=null){
                refresh = null
            }
            owner.lifecycle.removeObserver(this)
        }

}
