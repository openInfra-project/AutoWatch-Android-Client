package com.example.antiseptic

import com.example.antiseptic.data.*
import com.google.gson.annotations.Expose
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object RetrofitClient {
    val retrofit = Retrofit.Builder()
        //url 은 ngrok 사용으로 계속 달라짐.
        .baseUrl("https://1c7d7901512d.ngrok.io")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val signupservice: SignUpService = retrofit.create(SignUpService::class.java)
}

//서버로 보내는 INPUT데이터
interface SignUpService {
    //베이스 URL 을 제외한 경로
    @FormUrlEncoded
    @POST("/app_signup")
    fun requestSignUp(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): Call<DataSignUp>
    @Headers("Content-Type: application/json")
    @Multipart
    @POST("/app_image")
    fun requestImage(
        @Part image: MultipartBody.Part
    ): Call<DataImage2>

    //방 생성 POST 요청
    //방 입장 POST 요청
    @FormUrlEncoded
    @POST("/app_login")
    fun requestLoginIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DataSignUp>
    @FormUrlEncoded
    @POST("/app_delete")
    fun requestDelete(
        @Field("email") email: String
    ):Call<Int>
    @FormUrlEncoded
    @POST("/app_modify")
    fun requestModify(
        @Field("email") email: String,
        @Field("name") name: String
    ):Call<DataSignUp>
    @FormUrlEncoded
    @POST("home/app_roomnumber")
    fun requestRoomNumber(
        @Field("roomname") roomname:String
    ):Call<DataRoomNumber>
    @Multipart
    @POST("home/app_makeroom")
    fun requestMakeRoom(
        @Part files: MultipartBody.Part,
        @Part("name") name : RequestBody,
        @Part("pass")  pass : RequestBody,
        @Part("admin")  admin : RequestBody,
        @Part("checkbox")  checkbox : RequestBody
    ): Call<DataMakeRoom>
    @FormUrlEncoded
    @POST("home/app_makemyroom")
    fun requestRoomNumberPass(
        @Field("roomname") roomname:String,
        @Field("password") password:String,
        @Field("admin") admin:String,
        @Field("checkbox") checkbox:String
    ):Call<DataRoomNamePass>


}