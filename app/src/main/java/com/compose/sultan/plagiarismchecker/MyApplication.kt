package com.compose.sultan.plagiarismchecker
import  android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        appInstance = this

    }
    companion object {
        private lateinit var appInstance: MyApplication

        fun getAppInstance(): MyApplication {
            return appInstance
        }


    }
}