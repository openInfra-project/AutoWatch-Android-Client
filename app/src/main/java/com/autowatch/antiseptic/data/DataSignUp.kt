package com.autowatch.antiseptic.data

import java.io.Serializable
//서버에서 오는 데이터 응답 값.
data class DataSignUp(
    val email : String,
    val password: String,
    val name : String,
    val image : String

):Serializable