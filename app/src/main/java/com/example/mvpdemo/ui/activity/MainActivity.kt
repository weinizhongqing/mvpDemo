package com.example.mvpdemo.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpdemo.R
import com.example.mvpdemo.base.BaseMVPActivity
import com.example.mvpdemo.bean.NewsBean
import com.example.mvpdemo.contract.IMainModel
import com.example.mvpdemo.presenter.MainPresenter
import com.example.mvpdemo.ui.adapter.NewsAdapter
import com.example.mvpdemo.ui.inter.IBaseView

class MainActivity : BaseMVPActivity<MainPresenter>(), IMainModel{

    private lateinit var recyclerView: RecyclerView
    private val path: String =
        "https://i.news.qq.com/trpc.qqnews_web.kv_srv.kv_srv_http_proxy/list?sub_srv_id=24hours&srv_id=pc&offset=0&limit=40&strategy=1&ext={\"pool\":[\"top\"],\"is_filter\":7,\"check_type\":true}"

    private val mainPresenter by lazy {
        getPresenter()
    }

    override fun getLayoutId() = R.layout.activity_main


    override fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        mainPresenter.setIEventCallback(this)
    }

    override fun initData() {
        mainPresenter.getData(path)
    }

    override fun getPresenter(): MainPresenter = MainPresenter()


    override fun showDataBean(bean: MutableList<NewsBean>?) {
        val adapter = NewsAdapter(this, R.layout.news_item, bean!!)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener {
            Log.e("TAG", "showDataBean: "+bean[it].url)
            val bundle = Bundle()
            bundle.putString("data", bean[it].url)
            goActivity(WebViewActivity().javaClass, bundle)
        }
    }

    override fun showErrorMsg(msg: String) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }



}