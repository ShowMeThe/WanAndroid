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
import showmethe.github.core.util.toast.ToastFactory
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
/**
 * 仓库持有了 Activity 对象 需要在viewModel里释放 执行onClear() 否则viewModel 会出现暂时性内存泄露
 */
open class BaseRepository(var owner: LifecycleOwner? = null) :  DefaultLifecycleObserver {

    private var currentRetryCount = 0

    private  var refresh : WeakReference<SwipeRefreshLayout>? = null

    private var weakOwner :WeakReference<LifecycleOwner>? = null




    companion object {
        private const val maxConnectCount = 15
        private const val waitRetryTime = 3000
    }




    override fun onDestroy(owner: LifecycleOwner) {
        onClear()
    }

    fun init(owner: LifecycleOwner) : BaseRepository{
        weakOwner = WeakReference(owner)
        this.owner = weakOwner?.get()
        this.owner?.lifecycle?.addObserver(this)
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
        BaseApplication.ctx?.get()?.apply {
            if(this is BaseActivity<*,*>){
                this.showLoading()
            }
        }
    }

    fun dismissLoading(){
        BaseApplication.ctx?.get()?.apply {
            if(this is BaseActivity<*,*>){
                this.dismissLoading()
            }
        }
    }


        fun showToast(message: String) {
            ToastFactory.createToast(message)
        }

        fun filterOwner() : Boolean{
            return owner!=null
        }


        fun <T> applySchedulers(event: Lifecycle.Event = Lifecycle.Event.ON_STOP): ObservableTransformer<T, T> {
            val provider : LifecycleProvider<Lifecycle.Event> =
                AndroidLifecycle.createLifecycleProvider(owner)
            return ObservableTransformer {
                it.retryWhen { throwableObservable ->
                    throwableObservable.flatMap { throwable ->
                        if (throwable is IOException) {
                            currentRetryCount++
                            if (currentRetryCount < maxConnectCount) {
                                Observable.just(1).delay((waitRetryTime + currentRetryCount * 500).toLong(), TimeUnit.MILLISECONDS)
                            } else {
                                Observable.error<Any>(Throwable("Connection time out"))
                            }
                        } else {
                            Observable.error(throwable)
                        }
                    }
                }.filter { t -> t != null }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).compose(provider.bindUntilEvent<T>(event))
            }
        }


        fun <T> applyLongSchedulers(interval: Long = 15000): ObservableTransformer<T, T> {
            val inter = if (interval < 15000) 15000 else interval
            val provider : LifecycleProvider<Lifecycle.Event> =
                AndroidLifecycle.createLifecycleProvider(owner)
            return ObservableTransformer {
                it.repeatWhen { objectObservable ->
                    objectObservable.flatMap {
                        Observable.just(1).delay(inter, TimeUnit.MILLISECONDS)
                    }
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))
            }
        }


        /**
         * 适当使用避免造成内存泄漏
         */
        private fun onClear() {
            if(refresh!=null){
                refresh = null
            }
            if(owner!=null){
                owner?.lifecycle?.removeObserver(this)
                owner = null
                weakOwner = null
            }
        }

}
