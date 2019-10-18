package com.showmethe.wanandroid.entity

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR


/**
 * com.example.database.bean
 *
 *
 * 2019/5/17
 */
class RegisterBean : Observable {

    @get:Bindable
    var account: String = ""
        set(account) {
            field = account
            notifyChange(BR.account)
        }
    @get:Bindable
    var password: String = ""
        set(password) {
            field = password
            notifyChange(BR.password)
        }
    @get:Bindable
    var code: String = ""
        set(code) {
            field = code
            notifyChange(BR.code)
        }
    @Transient
    private var propertyChangeRegistry: PropertyChangeRegistry? = PropertyChangeRegistry()

    @Synchronized
    private fun notifyChange(propertyId: Int) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = PropertyChangeRegistry()
        }
        propertyChangeRegistry!!.notifyChange(this, propertyId)
    }

    @Synchronized
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = PropertyChangeRegistry()
        }
        propertyChangeRegistry!!.add(callback)

    }

    @Synchronized
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        if (propertyChangeRegistry != null) {
            propertyChangeRegistry!!.remove(callback)
        }
    }
}
