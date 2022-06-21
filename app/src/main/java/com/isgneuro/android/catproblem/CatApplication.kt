package com.isgneuro.android.catproblem

import android.app.Application

class CatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CatRepository.initialize(this)
    }
}