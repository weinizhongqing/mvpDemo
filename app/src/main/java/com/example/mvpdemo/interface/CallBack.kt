package com.example.mvpdemo.`interface`

/**
 *@Description
 *@created by xiaoma on 2020-12
 */
interface CallBack<K, V> {
    fun onSuccess(data: K?)
    fun onFail(data: V?)
}
