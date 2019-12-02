package showmethe.github.core.util.extras

import androidx.annotation.CheckResult
import androidx.lifecycle.MutableLiveData
import showmethe.github.core.util.match.isNotNull

/**
 * Author: showMeThe
 * Update Time: 2019/12/2 15:58
 * Package Name:showmethe.github.core.util.extras
 */
/**
 *  判断内容是否为空
 */
fun MutableLiveData<*>.valueIsNull() = this.value == null


/**
 * 简化MutableLiveData<Int> 加法
 */
infix fun MutableLiveData<Int>.plus(x:Int) : MutableLiveData<Int>{
    if(!this.valueIsNull()){
       postValue(value!!.plus(x))
    }
    return this
}

/**
 * 简化MutableLiveData<Int> 减法
 */
infix fun MutableLiveData<Int>.sub(x:Int) : MutableLiveData<Int>{
    if(!this.valueIsNull()){
        postValue(value!!.minus(x))
    }
    return this
}

/**
 * 简化MutableLiveData<T> postValue
 */
infix fun <T:Any> MutableLiveData<T>.post(newValue:T){
    this.postValue(newValue)
}

infix fun <T:Any> MutableLiveData<T>.set(newValue:T){
    this.value = newValue
}

infix fun <T:Any>MutableLiveData<T>.sameAs( equals : T) : Boolean{
    return if(value == null){
        false
    }else{
        value!!  ==  equals
    }
}

