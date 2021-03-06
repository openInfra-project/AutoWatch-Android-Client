package com.autowatch.antiseptic


import com.autowatch.antiseptic.data.*
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import java.util.Date
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object RetrofitClient {
    var gson = GsonBuilder()
        .setLenient()
        .create()

//    var okHttpClient = OkHttpClient().newBuilder()
//        .connectTimeout(40, TimeUnit.SECONDS)
//        .readTimeout(60, TimeUnit.SECONDS)
//        .writeTimeout(60, TimeUnit.SECONDS)
//        .build()

    val retrofit = Retrofit.Builder()
        //url 은 ngrok 사용으로 계속 달라짐.
//        .client(okHttpClient)
        .baseUrl("https://118.67.131.138:30000")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(getUnsafeOkHttpClient().build())
        .build()

    fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { hostname, session -> true }
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()


        return builder
    }

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
    //@Headers("Content-Type: application/json")
    @Multipart
    @POST("/app_image")
    fun requestImage(
        @Part image: MultipartBody.Part
    ): Call<DataImage2>
//    @Headers("Content-Type: application/json")
    @Multipart
    @POST("main/app_images")
    fun myrequestImage2(
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

    @Multipart
    @POST("main/app_makeroom")
    fun requestMakeRoom(
        @Part("name") name: RequestBody,
        @Part("pass") pass: RequestBody,
        @Part("admin") admin: RequestBody,
        @Part("checkbox") checkbox: RequestBody,
        @Part files: MultipartBody.Part
    ): Call<DataMakeRoom>
    @FormUrlEncoded
    @POST("main/app_makemyroom")
    fun requestRoomNumberPass(
        @Field("roomname") roomname: String,
        @Field("password") password: String,
        @Field("admin") admin: String,
        @Field("checkbox") checkbox: String
    ):Call<DataRoomNamePass>

    @FormUrlEncoded
    @POST("main/app_myroom")
    fun requestmyroom(
        @Field("email") email: String
    ):Call<List<DataMyRoomInfo>>
    @FormUrlEncoded
    @POST("main/app_enter_room")
    fun requestenterroom(
        @Field("roomname") roomname: String,
        @Field("password") password: String
    ):Call<DataRoomNumber>

    @FormUrlEncoded
    @POST("main/app_enter_myroom")
    fun requestentermyroom(
        @Field("roomname") roomname: String
    ):Call<DataRoomNumber>

    @FormUrlEncoded
    @POST("/app_mypage")
    fun requestMypage(
        @Field("email") email: String
    ): Call<DataMypage>

    @FormUrlEncoded
    @POST("main/app_check_myinfo")
    fun requestcheckmyinfo(

        @Field("room") room: String,
        @Field("number") number: String,
        @Field("name") name: String
    ):Call<DataRoomNamePass>

    @FormUrlEncoded
    @POST("/app_checkin")
    fun requestcheckin(
        @Field("email") email: String
    ): Call<DataRoomNumber>

    @FormUrlEncoded
    @POST("/app_checkout")
    fun requestcheckout(
        @Field("email") email: String
    ): Call<DataRoomNumber>

    @FormUrlEncoded
    @POST("/app_sendcount")
    fun requestsendcount(
        @Field("email") email: String,
        @Field("count") count: Int,
        @Field("nonperson") nonperson: Int,
        @Field("roomname") roomname: String
    ): Call<DataRoomNumber>
}