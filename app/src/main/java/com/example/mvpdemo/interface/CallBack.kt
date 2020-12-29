package com.example.mvpdemo.`interface`

import com.example.mvpdemo.bean.NewsBean

/**
 *@Description
 *@created by xiaoma on 2020-12
 */
interface CallBack<K,V>{
    fun onSuccess(data: K)
    fun onFail(msg: V)
}