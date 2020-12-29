package com.example.mvpdemo.base

import com.example.mvpdemo.ui.inter.IBaseView

open class BasePresenter<V:IBaseView> {

    var mIBaseView: V? = null

    fun setIEventCallback(eventCallback:V){
        this.mIBaseView = eventCallback
    }

    fun <E> callback(action: V.()->E): E? = mIBaseView?.run{
        action()
    }


}