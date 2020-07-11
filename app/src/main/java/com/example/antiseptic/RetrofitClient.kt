package com.example.antiseptic

import android.media.Image
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object RetrofitClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("aaaa")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val signupservice :SignUpService = retrofit.create(SignUpService::class.java)

}

//서버로 보내는 INPUT데이터
interface SignUpService {
    @FormUrlEncoded
    //베이스 URL 을 제외한 경로
    @POST("aasdasd")
    fun requestSignUp(
        @Field("Email") Email: String,
        @Field("Password") Password: String,
        @Field("Name") Name: String,
        @Field("Image") Image: Image
    ): Call<DataSignUp>
}