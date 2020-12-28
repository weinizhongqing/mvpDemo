package com.example.mvpdemo.contract

import com.example.mvpdemo.bean.NewsBean
import com.example.mvpdemo.ui.inter.IBaseView

interface IMainModel {

    fun getData(url:String,listener: OnGetDataListener)

    interface OnGetDataListener{
        fun onGetDataFinished(bean:MutableList<NewsBean>?)
        fun error(msg: String)
    }

    interface IMainView: IBaseView {
        fun showDataBean(bean:MutableList<NewsBean>?)

        fun showErrorMsg(msg: String)
    }

}