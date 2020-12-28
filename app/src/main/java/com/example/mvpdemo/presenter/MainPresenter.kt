package com.example.mvpdemo.presenter

import com.example.mvpdemo.base.BasePresenter
import com.example.mvpdemo.bean.NewsBean
import com.example.mvpdemo.contract.IMainModel
import com.example.mvpdemo.model.MainModel

class MainPresenter(private var mView:IMainModel.IMainView):BasePresenter<IMainModel.IMainView>(),IMainModel.OnGetDataListener{

    private var mModel:IMainModel?=null

    init {
        mModel = MainModel()
    }

    fun getData(url:String){
        this.mView.showLoading()
        this.mModel?.getData(url,this)
    }

    override fun onGetDataFinished(bean: MutableList<NewsBean>?) {
        this.mView.hideLoading()
        bean?.apply {
            mView.showDataBean(this)
        }
    }

    override fun error(msg: String) {
        mView.showErrorMsg(msg)
    }
}