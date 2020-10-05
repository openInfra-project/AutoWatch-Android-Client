package com.example.antiseptic.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataMyRoomInfo(
    @SerializedName("model")
    val model:String,
    val pk:Int,
    val fields:hash
)
data class hash(
    val room_member:String,
    val room_name:String,
    val room_ps:String
)


