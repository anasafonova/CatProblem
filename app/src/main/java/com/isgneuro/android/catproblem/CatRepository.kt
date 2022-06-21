package com.isgneuro.android.catproblem

import android.content.Context
import java.io.File

class CatRepository private constructor(context: Context) {
    private val filesDir = context.applicationContext.filesDir
    fun getPhotoFile(cat: Cat): File = File(filesDir, cat.photoFileName)

    companion object {
        private var INSTANCE: CatRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CatRepository(context)
            }
        }

        fun get(): CatRepository {
            return INSTANCE ?:
            throw IllegalStateException("CatRepository must be initialized")
        }
    }
}