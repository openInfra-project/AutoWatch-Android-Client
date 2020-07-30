package com.example.antiseptic

import android.media.Image
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object RetrofitClient {
    val retrofit = Retrofit.Builder()
        //url 은 ngrok 사용으로 계속 달라짐.
        .baseUrl("https://ae62aa549923.ngrok.io")
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
        //@Field("Image") Image: ArrayList<DataImage>
    ): Call<DataSignUp>

    //방 생성 POST 요청
    //방 입장 POST 요청
    @POST("/app_login")
    fun requestLoginIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Int>
    @DELETE("/app_login")
    fun requestDelete(
        @Field("email") email: String
    ):Call<Int>


}