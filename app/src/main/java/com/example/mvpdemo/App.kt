package com.example.mvpdemo

import android.app.Application

class App :Application(){

    companion object{
     private lateinit var application: App

        fun get(): App {
            return application
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}