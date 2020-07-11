package com.example.antiseptic

import android.media.Image
import java.io.Serializable
//서버에서 오는 데이터 응답 값.
class DataSignUp(
    val email : String,
    val password: String,
    val name : String,
    val image : Image
):Serializable