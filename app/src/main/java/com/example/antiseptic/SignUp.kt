package com.example.antiseptic

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    //ViewModel 직렬화를 할 수 없기에
    //imagedata 변수는 사진 선택 후 가져오는 데이터를 넣어줄 임의로 만들어준 변수
    private lateinit var imageData: List<com.esafirm.imagepicker.model.Image>
    private lateinit var body: MultipartBody.Part
    private val viewModel: DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //로그인 하러 가기
        text_goLogin.setOnClickListener {
            GoLogin()
        }
        //사진 선택
        btn_PhotoSelect.setOnClickListener {
            PhotoSelect()
        }
        //회원가입
        btn_enter_signup.setOnClickListener {
            if (edit_email.text.toString() != null && edit_password.text.toString() != null && edit_name.text.toString() != null && imageData != null) {
                //서버로 데이터를 보내줌
                senddata(
                    edit_email.text.toString(),
                    edit_password.text.toString(),
                    edit_name.text.toString(),
                    body
                )

            } else {
                Toast.makeText(this, "빈칸없이 입력해주세요", Toast.LENGTH_LONG).show()
            }


        }


    }


    //로그인 하러 가기
    private fun GoLogin() {
        startActivity(Intent(this, Login::class.java))
    }

    //사진 선택
    private fun PhotoSelect() {
        val intent = Intent(this, User_SignUp_PopUp::class.java)
        startActivityForResult(intent, 200)
    }

    //사진 선택 후 돌아오는 데이터 받기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                imageData =
                    data?.getSerializableExtra("image") as List<com.esafirm.imagepicker.model.Image>
                Toast.makeText(this, "" + imageData, Toast.LENGTH_SHORT).show()
                viewModel.setDataImage(imageData)
                text_goLogin.setText("" + viewModel.listimage)
                //일단 리스트에 있는 파일 1개를 가져옴 [0]
                val a: RequestBody =
                    RequestBody.create(MediaType.parse("image/jpeg"), viewModel.listimage[0])
                body = MultipartBody.Part.createFormData("image", "MyImage.jpg",a)
                text_goLogin.setText("" + body)

            }
        } else {

        }
    }


    //RetrofitClient 에 있는 메소드 사용
    private fun senddata(
        email: String,
        password: String,
        name: String,
        image: MultipartBody.Part
    ) {

        //업로드 중이라는 Dialog 띄어줌
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setTitle("업로드중...")
        progressDialog.show()

        RetrofitClient.signupservice.requestSignUp(
            email,
            password,
            name

            //image//이미지 데이터 여기에 넣기
        ).enqueue(object : Callback<DataSignUp> {
            override fun onFailure(call: Call<DataSignUp>, t: Throwable) {
                //실패시 작업 -> 간단하게 Dialog 띄어주기
                Toast.makeText(
                    applicationContext,
                    "회원가입 실패",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<DataSignUp>, response: Response<DataSignUp>) {
                text_goLogin.setText("" + response.body())
                //데이터를 받아서 저장해줌.

                if(response.body()!==null) {
                    val body = response.body()
                    val sharedPreference = getSharedPreferences("login", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreference.edit()
                    editor.putString("email",body?.email)
                    editor.commit()
                    viewModel.setData(response.body()!!)
                    Toast.makeText(applicationContext, "홈화면으로 이동합니다", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, Home::class.java))
                }else {

                }

            }
        })
//        RetrofitClient.signupservice.requestImage(body).enqueue(object : Callback<DataImage2> {
//            override fun onFailure(call: Call<DataImage2>, t: Throwable) {
//                Toast.makeText(
//                    applicationContext,
//                    "회원가입 실패",
//                    Toast.LENGTH_LONG
//                ).show()
//                text_goLogin.setText("" + t.message)
//            }
//
//            override fun onResponse(call: Call<DataImage2>, response: Response<DataImage2>) {
//                Toast.makeText(
//                    applicationContext,
//                    "성공",
//                    Toast.LENGTH_LONG
//                ).show()
//                text_goLogin.setText("성공+"+response.body())
//            }
//        })

    }
}