package com.example.mvpdemo.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpdemo.R
import com.example.mvpdemo.base.BaseMVPActivity
import com.example.mvpdemo.bean.NewsBean
import com.example.mvpdemo.contract.IMainModel
import com.example.mvpdemo.presenter.MainPresenter
import com.example.mvpdemo.ui.adapter.NewsAdapter

class MainActivity : BaseMVPActivity<IMainModel.IMainView, MainPresenter>(), IMainModel.IMainView {

    private lateinit var recyclerView: RecyclerView
    private val path: String =
        "https://i.news.qq.com/trpc.qqnews_web.kv_srv.kv_srv_http_proxy/list?sub_srv_id=24hours&srv_id=pc&offset=0&limit=20&strategy=1&ext={\"pool\":[\"top\"],\"is_filter\":7,\"check_type\":true}"

    private val mainPresenter by lazy {
        getPresenter()
    }

    override fun getLayoutId() = R.layout.activity_main


    override fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
    }

    override fun initData() {
        mainPresenter.getData(path)
    }

    override fun getPresenter(): MainPresenter = MainPresenter(this)


    override fun showDataBean(bean: MutableList<NewsBean>?) {
        Log.e("TAG", "showDataBean: ${bean.toString()}")
        val adapter = NewsAdapter(this, R.layout.news_item, bean!!)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener {
            Log.e("TAG", "showDataBean: ${bean[it].title}")
            Log.e("TAG", "showDataBean: ${bean[it].url}")
            val bundle = Bundle()
            bundle.putString("data", bean[it].url)
            goActivity(WebViewActivity().javaClass, bundle)
        }
    }

    override fun showErrorMsg(msg: String) {
        showToast(msg)
    }
}