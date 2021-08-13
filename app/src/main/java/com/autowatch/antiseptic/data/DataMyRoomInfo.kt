package com.autowatch.antiseptic.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class DataMyRoomInfo(
    @SerializedName("model")
    val model:String,
    val pk:Int,
    val fields:hash
)
data class hash(
    val mode:String,
    val room_name:String,
    val room_password:String,
    val make_date: Date
)


