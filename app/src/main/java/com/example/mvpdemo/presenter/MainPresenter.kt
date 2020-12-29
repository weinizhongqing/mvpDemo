package com.example.mvpdemo.presenter

import com.example.mvpdemo.`interface`.CallBack
import com.example.mvpdemo.base.BasePresenter
import com.example.mvpdemo.bean.NewsBean
import com.example.mvpdemo.contract.IMainModel
import com.example.mvpdemo.model.MainModel

class MainPresenter(private var mView: IMainModel) :
    BasePresenter<IMainModel>(), CallBack<MutableList<NewsBean>,String> {

    private var mModel: MainModel? = null

    init {
        mModel = MainModel()
    }

    fun getData(url: String) {
        this.mView.showLoading()
        this.mModel?.getData(url, this)
    }

    override fun onSuccess(data: MutableList<NewsBean>?) {
        this.mView.hideLoading()
        data?.apply {
            mView.showDataBean(this)
        }
    }


    override fun onFail(data: String?) {
        mView.showErrorMsg(data)
    }
}