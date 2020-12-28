package com.example.mvpdemo.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mvpdemo.App
import com.example.mvpdemo.R
import com.example.mvpdemo.ui.inter.IBaseView

@Suppress("UNCHECKED_CAST")
abstract class BaseMVPActivity<V, T : BasePresenter<V>> : AppCompatActivity(), IBaseView {
    private var baseLoadingView: BaseLoadingView? = null
    private var basePresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        basePresenter = getPresenter()
        basePresenter?.attachView(this as V)
        initLoadingView()
        initView()
        initData()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    abstract fun getPresenter(): T


    private fun initLoadingView() {
        baseLoadingView = BaseLoadingView(this, R.style.transparent_dialog)
    }

    private fun showLoadingView() {
        if (null != baseLoadingView) {
            baseLoadingView?.show()
        } else {
            initLoadingView()
            baseLoadingView?.show()
        }
    }

    private fun hideLoadingView() {
        if (null != baseLoadingView && baseLoadingView!!.isShowing) {
            baseLoadingView?.cancel()
        }
    }

    /**
     * 将显示dialog和取消dialog放在了基础类中
     */
    override fun showLoading() {
        showLoadingView()
    }

    override fun hideLoading() {
        hideLoadingView()
    }


    fun showToast(msg: String) {
        Toast.makeText(
            App.get(),
            msg, Toast.LENGTH_SHORT
        ).show()

    }

    fun goActivity(clazz: Class<Any>, bundle: Bundle) {
        val intent = Intent(this, clazz)
        intent.putExtras(bundle)
        startActivity(intent)
    }


}