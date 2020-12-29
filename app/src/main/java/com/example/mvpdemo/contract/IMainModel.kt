package com.example.mvpdemo.contract

import com.example.mvpdemo.bean.NewsBean
import com.example.mvpdemo.ui.inter.IBaseView

interface IMainModel : IBaseView{

    fun showDataBean(bean: MutableList<NewsBean>?)

    fun showErrorMsg(msg: String)

}