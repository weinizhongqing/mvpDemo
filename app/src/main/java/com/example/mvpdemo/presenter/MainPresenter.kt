package com.example.mvpdemo.presenter

import com.example.mvpdemo.`interface`.CallBack
import com.example.mvpdemo.base.BasePresenter
import com.example.mvpdemo.bean.NewsBean
import com.example.mvpdemo.contract.IMainModel
import com.example.mvpdemo.model.MainModel

class MainPresenter : BasePresenter<IMainModel>(){

    private var mModel: MainModel? = null

    init {
        mModel = MainModel()
    }

    //view层调用方法
    fun getData(url: String) {

        callback {
            //显示加载框
            showLoading()
            //调用model层方法返回数据
            mModel?.getData(url, object : CallBack<MutableList<NewsBean>,String>{
                override fun onSuccess(data: MutableList<NewsBean>) {
                    hideLoading()
                    showDataBean(data)
                }
                override fun onFail(msg: String) {
                    hideLoading()
                    showErrorMsg(msg)
                }
            })
        }





    }





}