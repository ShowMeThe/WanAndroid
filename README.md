#### 简书地址：https://www.jianshu.com/p/03e7446ff512
#### 新的UI</br>
<img src= "https://github.com/ShowMeThe/WanAndroid/blob/master/jpg/20191114.gif" width = 200 alt= "ui"/></br>
#### 基于AAC架构玩安卓客户端</br>
#### 首页、公众号、知识体系、项目</br>  
___
<img src ="https://github.com/ShowMeThe/WanAndroid/blob/master/jpg/20191021094102.jpg" width = 200 alt = "首页"/> <img src ="https://github.com/ShowMeThe/WanAndroid/blob/master/jpg/20191021094100.jpg" width = 200 alt = "公众号"/>  <img src ="https://github.com/ShowMeThe/WanAndroid/blob/master/jpg/20191021094056.jpg" width = 200 alt = "知识体系"/>  <img src ="https://github.com/ShowMeThe/WanAndroid/blob/master/jpg/20191021094105.jpg" width = 200 alt = "项目"/>
___
#### 网络请求  
还是常用大名鼎鼎的Retrofit2，然后结合了coroutines，使用起来更加方便了  
举个栗子：  
接口只需要使用suspend即可
```kotlin
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username:String,@Field("password") password:String) : Response<JsonResult<Auth>>
 ```
 处理请求的逻辑封装了另一个类CallResult，只显示关键部分
 ```kotlin
    fun hold(result: suspend () -> Response<JsonResult<T>>): CallResult<T> {
        var response: Response<JsonResult<T>>?
        var netJob: Job? = null
        owner?.apply {
            netJob = lifecycleScope.launchWhenStarted {
                __________ 处理loading状态 ————————————————
                response  = withContext(Dispatchers.IO) {
                    withTimeoutOrNull(10000){//超时处理
                        result.invoke() //网络请求
                    }
                }
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    withContext(Dispatchers.Main) {
                        __________ 处理超时和返回结果的逻辑————————————————
                    }
                } else {
                    netJob?.cancel()
                }
            }
        }
        return this
    }
 
  ```
  使用方式如下：
  
  ```kotlin
    fun login(username:String,password:String,call:MutableLiveData<Result<Auth>>){
        CallResult<Auth>(owner){
             loading {
                 __________ 处理读取————————————————
            }.success { result, message ->
                __________ 处理成功————————————————
                call.value = result
            }.error { result, code, message ->
                 __________ 处理错误————————————————
                call.value = result
            }.outTime {
                  __________ 处理超时————————————————
                call.value = it
            }.hold {
                api.login(username, password)//登录
            }
      
        }
          
    }
   ```
  该项目使用: LiveData、ViewModel、Room 、Databinding 、WorkManager 、Lifecycles  
  图片加载采用: Glide  
  消息总线采用了：live-event-bus github：https://github.com/JeremyLiao/LiveEventBus  
  使用到了SlideBack的返回动画,fork后我进行进修改,原项目github:https://github.com/ParfoisMeng/SlideBack  
  其中照片墙部分不属于玩安卓的内容，是我本人自行加上，属于本地内容，利用Room数据库完成基本的数据获取。
  #### 超时处理
  首页的文章图片新增了超时处理的代码，但由于有缓冲和wanandroid Api反应快，效果不明显，测试起来比较麻烦，直接看日志或者打断点比较好校验，
  也可以自己在电脑起一个接口，设置等待时长来测试。至于网络不好，超时后重新提交的设计暂时不加入。不过这个功能在传统的Rxjava2+Retrofit实现起来很方便的。
  #### 增加了快捷标签的入口
  
  ### 更新日志  
  #### V1.05 修改时间：2019/12/4
  内容：逐步进行DSL化,并把Retrofit的接口进行单例</br>
  ##### 一、Retrofit的接口进行单例（—————其实这种单例是很简单，我都不知道我当时写的时候为啥搞得那么复杂——————）  
  例子：</br>
  ```kotlin
     ___________________初始化_______________
    startInit {
            modules(Module{
                single{ RetroHttp.createApi(auth::class.java) }
            },
                Module{
                single{  RetroHttp.createApi(main::class.java) }
            })
        }
     ————————————————调用得地方————————————
     
     val api: main by inject() //获取main.class的实例

 ```
  ##### 二、修改了TextWatcher
  ```kotlin
   fun EditText.textWatcher(textWatch: SimpleTextWatcher.()->Unit){
    val simpleTextWatcher = SimpleTextWatcher()
    textWatch.invoke(simpleTextWatcher)
    addTextChangedListener(object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            simpleTextWatcher.afterText?.invoke(s,s)  }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            simpleTextWatcher.beforeText?.invoke(s, start, count, after) }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            simpleTextWatcher.onTextChanged?.invoke(s, start, before, count) }
       })
   }

 class  SimpleTextWatcher() {
     var afterText: (Editable?.(s: Editable?)->Unit)? = null
     fun afterTextChanged(afterText: (Editable?.(s: Editable?)->Unit)){
         this.afterText = afterText }
     var beforeText:((s: CharSequence?, start: Int, count: Int, after: Int)->Unit)? = null
     fun beforeTextChanged(beforeText:((s: CharSequence?, start: Int, count: Int, after: Int)->Unit)){
         this.beforeText = beforeText }
     var  onTextChanged : ((s: CharSequence?, start: Int, before: Int, count: Int)->Unit)? = null
     fun onTextChanged(onTextChanged : ((s: CharSequence?, start: Int, before: Int, count: Int)->Unit)){
         this.onTextChanged = onTextChanged}
}
  ```
  #### V1.05 修改时间：2019/11/14
  内容：重构了登录和注册UI
  #### V1.03 修改时间：2019/11/10
  内容：添加一个预加载占位的drawable,尚未完善优化，效果如下  
  <img src= "https://github.com/ShowMeThe/WanAndroid/blob/master/jpg/20191110.gif" width = "300" alt ="Gif" />
  #### V1.03 修改时间：2019/11/7
  内容：修复冷启动慢得问题，在启动时候，使用了反射来解决权限申请问题，此处是很大的问题，再加上Kotlin的Reflect比Java的Reflect还慢  
  导致启动到看到SplashActivity高达2s之久。
  #### V1.03 修改时间：2019/11/2
  内容：新增一个FloatActonButton的菜单，实现的效果其实不难，效果如下：  
  <img src ="https://github.com/ShowMeThe/WanAndroid/blob/master/jpg/20191102234438.jpg" width = 200 alt = "主页"/>  
  简单说一下步骤吧，地址：https://github.com/ShowMeThe/SpeedDial
  1、通过自定义的Beahavior 可以很简单地拿到需要依附的View
  ```kotlin
  override fun layoutDependsOn(parent: CoordinatorLayout, child: ExpandMenuChildLayout, dependency: View): Boolean {
        return dependency is FloatingActionButton
    }
  ```
  2、对自己控制的view和依附的dependency构建关系，相对位置，状态改变监听等等，代码在ExpandBottomBehavior.kt里
  ```kotlin
     override fun dependentViewChanged(parent: CoordinatorLayout, child: CircularRevealLinearLayout, dependency: View) {
        val dependencyWidth = dependency.measuredWidth
        val dependencyHeight = dependency.measuredHeight
        val childHeight = child.measuredHeight
        val childWidth = child.measuredWidth
        val dependencyX = dependency.x
        val dependencyY = dependency.y + dependencyHeight

        child.x = dependencyX + dependencyWidth - childWidth
        child.y = dependencyY -  defaultMargin - dependencyHeight - childHeight
    }

  ```
  3 、就是在这个view上实现常规自定view代码编写，其实核心是需要理解Beahavior的用处，CoordinatorLayout里面这个是一个很灵活的操作，
  网上有非常多的例子介绍怎么使用，也是很常规的例子。  
  然后略改了一下之前添加Glide图片加载效果，让图片加载过渡时候有个渐变色效果，代码如下:  
  ```kotlin
  override fun transition(current: Drawable, adapter: Transition.ViewAdapter): Boolean {
        var previous = adapter.currentDrawable
        if (previous == null) {
            previous = ColorDrawable(Palette.from(createBitmap(current)).generate().getVibrantColor(Color.TRANSPARENT))
        }
       ——————————  一堆实现动画的代码 ——————————
        return true
    }

    private fun createBitmap(current: Drawable) : Bitmap{
        val bitmap = Bitmap.createBitmap(10,10,Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        current.setBounds(0,0,10,10)
        current.draw(canvas)
        return bitmap
    }
  ```
  #### V1.03 修改时间：2019/10/29
  内容： 新增一个可以监听到下载进度的ImageView,继承PhotpView，利用onDraw，画出进度条。新增Glide的Okhttp3图片Loader的自定义进度监听。
  自定义属性为zoomable，默认为false
  新增一个注解的权限管理，利用Fragment处理权限申请。在权限申请的内部，仅提供了申请结果。暂不实现权限被拒绝后吊起一个对话框告知用户手动申请， 
  个人理由是因为在权限开发这块我们更应该倾向于某功能需要用到权限时候才执行权限申请，而不是一次性全部申请。所以应该是尽可能保留功能稳定完善，
  减少提示框的弹出。  
  #### V1.02 修改时间：2019/10/25   
  内容：修复Glide的过渡动画，会导致某些时候图片加载完后，被拉伸了。
  修改图片墙的FAB位置

 
  
   
    
   
