package com.example.antiseptic

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    val data = ArrayList<DataSignUp>()
    val dataImage = ArrayList<DataImage>()
    val LiveData = MutableLiveData<ArrayList<DataSignUp>>()
    val LiveDataImage = MutableLiveData<ArrayList<DataImage>>()

    fun setDataImage(uri : Uri) {
        val dataImage = DataImage(uri)
    }

}