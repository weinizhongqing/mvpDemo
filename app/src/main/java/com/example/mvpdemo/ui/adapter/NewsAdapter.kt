package com.example.mvpdemo.ui.adapter

import android.content.Context
import android.widget.TextView
import com.example.mvpdemo.R
import com.example.mvpdemo.base.BaseAdapter
import com.example.mvpdemo.base.BaseHolder
import com.example.mvpdemo.bean.NewsBean

class NewsAdapter(ctx: Context, layoutRes: Int, mData: MutableList<NewsBean>) :
    BaseAdapter<NewsBean>(ctx, layoutRes, mData) {
    override fun convert(holder: BaseHolder, position: Int) {
        holder.getView<TextView>(R.id.tx_title).apply {
            text = mData[position].title
        }
    }
}