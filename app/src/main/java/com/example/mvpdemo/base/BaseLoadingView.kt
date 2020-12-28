package com.example.mvpdemo.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.mvpdemo.R
import com.wang.avi.AVLoadingIndicatorView


class BaseLoadingView(context: Context, themeResId: Int) : AlertDialog(context, themeResId) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_loading_view)
        val loadingView = findViewById<AVLoadingIndicatorView>(R.id.loadingView)
        loadingView?.smoothToShow()

        // 点击屏幕，dialog不消失
        setCanceledOnTouchOutside(false)
    }
}