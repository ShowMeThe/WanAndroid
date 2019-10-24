## 基于AAC架构玩安卓客户端
预览图如下：  
#### 首页、公众号、知识体系、项目
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
        CallResult<Auth>(owner)
            .loading {
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
   ```
  该项目使用: LiveData、ViewModel、Room 、Databinding 、WorkManager 、Lifecycles  
  图片加载采用: Glide  
  消息总线采用了：live-event-bus github：https://github.com/JeremyLiao/LiveEventBus  
  使用到了SlideBack的返回动画,fork后我进行进修改,原项目github:https://github.com/ParfoisMeng/SlideBack  
  其中照片墙部分不属于玩安卓的内容，是我本人自行加上，属于本地内容，利用Room数据库完成基本的数据获取。
  #### 超时处理
  首页的文章图片新增了超时处理的代码，但由于有缓冲和wanandroid Api反应快，效果不明显，测试起来比较麻烦，直接看日志或者打断点比较好校验，
  也可以自己在电脑起一个接口，设置等待时长来测试。至于网络不好，超时后重新提交的设计暂时不加入。不过这个功能在传统的Rxjava2+Retrofit实现起来很方便的。
 
  
   
    
   
