package com.example.mvpdemo.model

import com.example.mvpdemo.App
import com.example.mvpdemo.`interface`.CallBack
import com.example.mvpdemo.bean.NewsBean
import com.example.mvpdemo.http.OkHttpUtils
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainModel {

    private var newsBeanList = mutableListOf<NewsBean>()

    fun getData(url: String, callBack: CallBack<MutableList<NewsBean>, String>) {
        OkHttpUtils.getInstance(App.get())?.getRequest(
                url,
                null,
                object : OkHttpUtils.OkHttpCallback {
                    override fun onResponse(response: Response) {
                        val json = response.body?.string()
                        json?.apply {
                            val jb = JSONObject(this)
                            val data = jb.getString("data")
                            val list = JSONObject(data)
                            val lists = list.getJSONArray("list")
                            for (i in 0 until lists.length()) {
                                val obj: JSONObject = lists.get(i) as JSONObject
                                val title = obj.getString("title")
                                val urls = obj.getString("url")
                                val newsBean = NewsBean(title, urls)
                                newsBeanList.add(newsBean)
                            }
                            callBack.onSuccess(newsBeanList)
                        }

                    }

                    override fun onError(e: IOException) {
                        callBack.onFail(e.message!!)
                    }
                })
    }

}