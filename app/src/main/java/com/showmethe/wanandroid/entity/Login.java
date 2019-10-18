package com.showmethe.wanandroid.entity;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;

import com.showmethe.wanandroid.BR;


/**
 * com.ken.materialwanandroid.entity
 * <p>
 * 2019/9/4
 **/
public class Login implements Observable {

    private String account = "";
    private String password = "";
    private boolean enable = false;

    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    @Bindable
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        setEnable(!(account.isEmpty()||(password.isEmpty())));
        notifyChange(com.showmethe.wanandroid.BR.account);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        setEnable(!(account.isEmpty()||(password.isEmpty())));
        notifyChange(com.showmethe.wanandroid.BR.password);
    }

    private synchronized void notifyChange(int propertyId) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.notifyChange(this, propertyId);
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.add(callback);

    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry != null) {
            propertyChangeRegistry.remove(callback);
        }
    }

    @Bindable
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        notifyChange(com.showmethe.wanandroid.BR.enable);
    }
}
