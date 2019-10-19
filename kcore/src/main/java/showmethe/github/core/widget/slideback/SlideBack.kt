package showmethe.github.core.widget.slideback

import android.app.Activity
import androidx.annotation.IntDef
import java.util.*


class SlideBack {

  companion object{

      // 使用WeakHashMap防止内存泄漏
      private val map = WeakHashMap<Activity, SlideBackManager>()


      /**
       * 侧滑返回模式 左
       */
      const val EDGE_LEFT = 0x0000

      /**
       * 侧滑返回模式 右
       */
      const val EDGE_RIGHT = 0x0001

      /**
       * 侧滑返回模式 左右皆可
       */
      const val EDGE_BOTH = 0x0002


      /**
       * 箭头动画
       */
      const   val ANIM_CLASSIC = 0x0005

      const  val ANIM_ROTATE = 0x0006


      /**
       * 注册
       *
       * @param activity 目标Act
       * @param callBack 回调
       */
      fun register(activity: Activity,callBack :()->Unit) {
          register(activity, false, callBack)
      }

      /**
       * 注册
       *
       * @param activity   目标Act
       * @param haveScroll 页面是否有滑动
       * @param callBack   回调
       */
      fun register(activity: Activity, haveScroll: Boolean, callBack :()->Unit) {
          with(activity).haveScroll(haveScroll).callBack(callBack).register()
      }

      /**
       * 注销
       *
       * @param activity 目标Act
       */
      fun unregister(activity: Activity) {
          val slideBack = map[activity]
          slideBack?.unregister()
          map.remove(activity)
      }

      /**
       * 构建侧滑管理器 - 用于更丰富的自定义配置
       *
       * @param activity 目标Act
       * @return 构建管理器
       */
      fun with(activity: Activity): SlideBackManager {
          val manager = SlideBackManager(activity)
          map[activity] = manager
          return manager
      }
  }



    @IntDef(EDGE_LEFT, EDGE_RIGHT, EDGE_BOTH)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class EdgeMode


    /**
     * 侧滑箭头动画
     */
    @IntDef(ANIM_CLASSIC, ANIM_ROTATE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class AnimMode

}