package com.isgneuro.android.catproblem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isgneuro.android.catproblem.api.CatFetcher
import java.io.File
import java.util.*

class CatDetailViewModel: ViewModel() {
    private val catRepository = CatRepository.get()
    //val catParametersLiveData: LiveData<List<String>>
    //private val catIDLiveData = MutableLiveData<UUID>()

    init {
        //catParametersLiveData =
        CatFetcher().fetchCatParameters()
    }

    fun getPhotoFile(cat: Cat): File {
        return catRepository.getPhotoFile(cat)
    }

//    fun loadCat(catID: UUID) {
//        catIDLiveData.value = catID
//    }
//
//    fun saveCrime(cat: Cat) {
//        catRepository.updateCat(cat)
//    }
}