package com.example.mvpdemo.http

import android.content.Context
import android.os.Handler
import android.os.Looper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkHttpUtils(context: Context) {

    companion object {

        private var mOkHttpUtils: OkHttpUtils? = null
        fun getInstance(context: Context): OkHttpUtils? {
            if (mOkHttpUtils == null) {
                synchronized(OkHttpUtils::class.java) {
                    if (mOkHttpUtils == null) {
                        mOkHttpUtils = OkHttpUtils(context)
                    }
                }
            }
            return mOkHttpUtils
        }
    }

    init {
        into(context)
    }

    @Volatile
    private lateinit var mOkHttpClient: OkHttpClient
    private lateinit var mHandler: Handler


    private fun into(context: Context): OkHttpUtils {
        //缓存的文件夹
        val fileCache = File(context.externalCacheDir, "response")
        val cacheSize: Long = 10 * 1024 * 1024//缓存大小为10M
        val cache = Cache(fileCache, cacheSize)
        //进行OkHttpClient的一些设置
        mOkHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                //设置缓存
                .cache(cache)
                .build()
        mHandler = Handler(Looper.getMainLooper())
        return this
    }


    /**
     * get请求
     * @param url:String
     * @param map:HashMap<String,Any>?
     * @param callback:OkHttpCallback
     */
    fun getRequest(url: String, map: HashMap<String, Any>?, callback: OkHttpCallback) {
        val stringBuilder = StringBuilder(url)
        val entries = map?.entries
        if (entries != null) {
            for (entry in entries) {
                stringBuilder.append(entry)
                stringBuilder.append("&")
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        }
        val request = Request.Builder()
                .get()
                .url(stringBuilder.toString())
                .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //在UI线程中执行回调
                mHandler.post { callback.onError(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //在UI线程中执行回调
                mHandler.post { callback.onResponse(response) }
            }
        })
    }

    /**
     * postJson
     * @param url: String
     * @param json:String
     * @param callback: OkHttpCallback
     */

    fun postJson(url: String, json: String, callback: OkHttpCallback) {
        val mediaType = "application/json;charset=utf-8".toMediaTypeOrNull()
        val body = json.toRequestBody(mediaType)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //在UI线程中执行回调
                mHandler.post { callback.onError(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //在UI线程中执行回调
                mHandler.post { callback.onResponse(response) }
            }
        })
    }


    /**
     * postForm
     * @param url:String
     * @param map:HashMap<String, Any>?
     * @param map:callback: OkHttpCallback
     */

    fun postForm(url: String, map: HashMap<String, Any>, callback: OkHttpCallback) {
        val body = buildParams(map)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //在UI线程中执行回调
                mHandler.post { callback.onError(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //在UI线程中执行回调
                mHandler.post { callback.onResponse(response) }
            }
        })
    }

    /**
     * 加载文件
     * @param url:String
     * @param callback: OkHttpCallback
     */

    fun loadFile(url: String, callback: OkHttpCallback) {
        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //在UI线程中执行回调
                mHandler.post { callback.onError(e) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //在UI线程中执行回调
                mHandler.post { callback.onResponse(response) }
            }
        })
    }


    /**
     * 异步文件参数混合上传
     * @param  url: String
     * @param params: Map<String, String>?
     * @param  file: File
     * @param  callback: OkHttpCallback 响应回调
     * @param   progressListener: ProgressRequestBody 进度回调
     */
    fun upLoadFileAndParams(url: String, type: String, params: Map<String, Any>?, file: File, callback: OkHttpCallback, progressListener: ProgressRequestBody.ProgressListener) {

        val requestBody = file.asRequestBody("text/x-markdown; charset=utf-8".toMediaType())//表示任意二进制流
        //因为是文件参数混合上传，所以要分开构建
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (params?.isEmpty() == true) {
            for ((key, value) in params) {
                builder.addFormDataPart(key, value.toString())
            }
        }
        val multipartBody = builder
                .addFormDataPart(type, file.name, requestBody)
                .build()
        val countingRequestBody = ProgressRequestBody(multipartBody, progressListener)
        val request = Request.Builder()
                .url(url)
                .post(countingRequestBody)
                .build()
        val call = mOkHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //在UI线程中执行回调
                mHandler.post { callback.onError(e) }
            }

            override fun onResponse(call: Call, response: Response) {
                //在UI线程中执行回调
                mHandler.post { callback.onResponse(response) }
            }
        })
    }


    /**
     * 参数添加到表单中
     * @param param: Map<String, String>?
     * @return RequestBody
     */
    private fun buildParams(param: Map<String, Any>?): RequestBody {
        var params: Map<String, Any>? = param
        if (params == null) {
            params = HashMap()
        }
        val builder = FormBody.Builder()
        for (entry in params.entries) {
            val key = entry.key
            var value: Any? = entry.value
            if (value == null) {
                value = ""
            }
            builder.add(key, value.toString())
        }
        return builder.build()
    }


    interface OkHttpCallback {
        fun onResponse(response: Response)
        fun onError(e: IOException)
    }


    /*文件加载监听*/
    class ProgressRequestBody(body: RequestBody, listener: ProgressListener) : RequestBody() {
        private var mListener: ProgressListener? = listener
        private var mBody: RequestBody? = body
        private var mProgressSink: ProgressSink? = null
        private var mBufferedSink: BufferedSink? = null

        override fun contentType(): MediaType? {
            return mBody?.contentType()
        }

        override fun writeTo(sink: BufferedSink) {
            //将Sink重新构造
            mProgressSink = ProgressSink(sink)
            if (mBufferedSink == null) {
                //创建输出流体系  mBufferedSink = Okio.buffer(mProgressSink);
                mBufferedSink = mProgressSink?.buffer()
            }
            //进行流输出操作
            mBufferedSink?.let { mBody?.writeTo(it) }
            mBufferedSink?.flush()
        }

        override fun contentLength(): Long {
            return try {
                mBody!!.contentLength()
            } catch (e: IOException) {
                -1
            }

        }

        internal inner class ProgressSink(delegate: Sink) : ForwardingSink(delegate) {
            private var byteWrite: Long = 0//当前写入的字节

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                //必须执行父类方法，否则无法上传
                super.write(source, byteCount)
                byteWrite += byteCount
                if (mListener != null) {
                    //更新进度
                    mListener?.onProgress(byteWrite, contentLength())
                }
            }
        }

        interface ProgressListener {
            fun onProgress(byteWrite: Long, contentLength: Long)
        }
    }


}