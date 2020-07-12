package com.example.antiseptic

import android.media.Image
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    val data = ArrayList<DataSignUp>()
    var dataImage = ArrayList<DataImage>()
    val LiveData = MutableLiveData<ArrayList<DataSignUp>>()
    val LiveDataImage = MutableLiveData<ArrayList<DataImage>>()

    fun setDataImage(item : DataImage ) {
        //중복되어서 붙여지므로 지워주고 시작함.
        dataImage.clear()
        dataImage.add(item)
        LiveDataImage.value = dataImage
    }

    fun deleteDataImage(position: Int) {
//        dataImage.removeAt(position)
//        LiveDataImage.value = dataImage

    }
}