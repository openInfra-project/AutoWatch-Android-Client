package com.autowatch.antiseptic

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log

import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.autowatch.antiseptic.data.DataImage2
import com.autowatch.antiseptic.data.DataRoomNumber
import com.autowatch.antiseptic.data.DataViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_delete_dialog.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.main_drawer_header_logged_in.*
import kotlinx.android.synthetic.main.main_drawer_header_logged_in.view.*
import kotlinx.android.synthetic.main.main_drawer_header_logged_out.*
import kotlinx.android.synthetic.main.main_include_drawer.*
import kotlinx.android.synthetic.main.nav_userheader.*
import kotlinx.android.synthetic.main.nav_userheader.btn_nav_close
import me.piruin.quickaction.QuickAction
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Home : AppCompatActivity() {
    private var its: Boolean = true
    private val viewModel: DataViewModel by viewModels()
    private lateinit var imageData: List<com.esafirm.imagepicker.model.Image>
    private lateinit var body: MultipartBody.Part
    private val QuickAction: QuickAction? = null
    private val QuickIntent: QuickAction? = null
    private var dbemail: String? = null
    private var dbpassword: String? = null
    private var dbname: String? = null
    private var dbimage: String? = null
    private var roompassword: String? = null
    private var roomname: String? = null
    private var custom : Dialog? =null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_include_drawer)
        val loginDB = loginDB(context = applicationContext)
//        code_visible.visibility = View.GONE
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
            dbpassword = dbjson.getString("password") ?: null
            dbname = dbjson.getString("name") ?: null
            dbimage = dbjson.getString("image") ?: null
        } else {

        }


        if (dbemail != null && dbpassword != null && dbname != null) {
            login()
        } else {
            logout()
        }



        //뒤로가기 버튼
        //방입장 버튼 클릭시
        btn_home_join.setOnClickListener {
//            homeAnimation(it = its)
            var builder = AlertDialog.Builder(this)
            builder.setTitle("ENTER ROOM")

            var v1 = layoutInflater.inflate(R.layout.enterroom_diaglog, null)
            builder.setView(v1)

            // p0에 해당 AlertDialog가 들어온다. findViewById를 통해 view를 가져와서 사용
            var listener = DialogInterface.OnClickListener { p0, p1 ->
                var alert = p0 as AlertDialog
                var edit1: EditText? = alert.findViewById<EditText>(R.id.editText)
                var edit2: EditText? = alert.findViewById<EditText>(R.id.editText2)

                if(edit1?.text.toString()!=""){
                    if(edit2?.text.toString()!=""){

                        enterroom(edit1?.text.toString(), edit2?.text.toString())
                    }else{
                        Toast.makeText(this, "방 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
                    }
                }else
                    Toast.makeText(this, "방 이름을 입력해주세요", Toast.LENGTH_LONG).show()



            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)

            builder.show()

        }
        //방 만들기

        btn_home_create.setOnClickListener {
            startActivity(Intent(this, ManagerRoomPopUp::class.java))
        }



        //menu 애니메이션
        btn_home_menu.setOnClickListener {
            main_drawer_layout.openDrawer((GravityCompat.START))
            val imageurl ="https://118.67.131.138:30000/media/"+dbimage
            Log.d("사용자이미지1",dbimage)
            Log.d("사용자이미지2",imageurl)
            Glide.with(this@Home).load(imageurl).apply(
                RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
            ).into(main_header_include_logged_in.iv_image);
        }
        btn_nav_close.setOnClickListener {
            main_drawer_layout.closeDrawers()
        }

        main_navigation_btn3.setOnClickListener {
            loginDB.deleteDB()
            logout()
            startActivity(Intent(this, Login::class.java))
        }

        navigation_header_login_btn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        main_navigation_btn1.setOnClickListener {
            //버튼1 클릭 시  마이페이지
            startActivity(Intent(this, Mypage::class.java))
        }

        main_navigation_btn2.setOnClickListener {
            //버튼2 클릭 시 이미지변경
            PhotoSelect()
        }

        main_navigation_btn4.setOnClickListener {
            //버튼3 클릭 시 회원탈퇴
            custom = DeleteDialog(context = this)
            (custom as DeleteDialog).show()
            (custom as DeleteDialog).btn_yes.setOnClickListener{
                deleteUser()
                startActivities(arrayOf(Intent(this, Login::class.java)))


            }

        }
//        //nav 학생정보입력
//        btn_nav_mypage.setOnClickListener {
//            startActivity(Intent(this, Mypage::class.java))
//        }

//        //로그아웃
//        btn_nav_logout.setOnClickListener {
//            loginDB.deleteDB()
//            logout()
//            startActivity(Intent(this, Home::class.java))
//        }
//        //로그인하러 가기
//        btn_nav_visible.setOnClickListener {
//            startActivity(Intent(this, Login::class.java))
//        }
        //회원탈퇴
//        btn_home_deleteuser.setOnClickListener {
//            custom = DeleteDialog(context = this)
//            (custom as DeleteDialog).show()
//            (custom as DeleteDialog).btn_yes.setOnClickListener{
//                    deleteUser()
//                    startActivities(arrayOf(Intent(this, Login::class.java)))
//
//
//            }
//
//
//        }


        //deleteUser()


//        drawer_view.setOnTouchListener { v, event -> true }


//
//        //사진선택기능
//        btn_home_changeimage.setOnClickListener {
//            PhotoSelect()
//        }



        //방목록
        btn_myroom.setOnClickListener {
            startActivity(Intent(this, NavRoom::class.java))
        }


    }



    fun enterroom(roomname: String, password: String) {
        RetrofitClient.signupservice.requestenterroom(roomname, password)
            .enqueue(object :
                retrofit2.Callback<DataRoomNumber> {
                override fun onFailure(call: Call<DataRoomNumber>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "전송 실패"+t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                override fun onResponse(
                    call: Call<DataRoomNumber>,
                    response: Response<DataRoomNumber>
                ) {
                    Toast.makeText(
                        applicationContext,
                        "입장"+response.body(),
                        Toast.LENGTH_LONG
                    ).show()
                    val body = response.body()
                    Log.d("방입장",body?.roomname)

                    if(body!=null) {
//                        btn_home_inner.setText(""+body.roomname)
                        if(body.roomname=="EXAM") {
                            Toast.makeText(
                                applicationContext,
                                "얼굴 인식 페이지로 이동합니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("방입장!!!!!!!!!!",roomname)
                            val intent = Intent(applicationContext, Checkmyinfo::class.java)
                            intent.putExtra("roomname", roomname)
                            startActivity(intent)
                        }else if(body.roomname=="STUDY"){
                            //바로 방입장
                            Toast.makeText(
                                applicationContext,
                                "방 입장합니다",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(applicationContext, Blockapp_study::class.java)
                            intent.putExtra("roomname", roomname)
                            startActivity(intent)
                        }else if(body.roomname=="Fail") {
                            Toast.makeText(
                                applicationContext,
                                "방 비밀번호가 틀립니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(body.roomname=="None") {
                            Toast.makeText(
                                applicationContext,
                                "해당 방이 없습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }else {
                            Toast.makeText(
                                applicationContext,
                                "?",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }else {
                        Toast.makeText(
                            applicationContext,
                            "실패"+response.body(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
    }

    fun logout() {
        main_header_include_logged_in.visibility = View.INVISIBLE
        main_header_include_logged_out.visibility = View.VISIBLE
        main_navigation_btn1.visibility = View.INVISIBLE
        main_navigation_btn2.visibility = View.INVISIBLE
        main_navigation_btn3.visibility = View.INVISIBLE
        main_navigation_btn4.visibility = View.INVISIBLE
    }

    fun login() {


        main_header_include_logged_in.iv_image.setImageResource(R.drawable.no_phone)
        main_header_include_logged_in.visibility = View.VISIBLE
        main_header_include_logged_out.visibility = View.INVISIBLE
        main_header_include_logged_in.tv_name.setText(dbname)
        main_navigation_btn1.isEnabled = true
        main_navigation_btn2.isEnabled = true
        main_navigation_btn3.isEnabled = true
        main_navigation_btn4.isEnabled = true
//        //로그인시 화면
//        text_nav_login.setText("환영합니다")
//        text_nav_name.setText("" + dbname + "님")
//        //로그아웃 시 다시 Home 으로 이동 후 바뀐 nav 보여줌
//        linear_nav_login.visibility = View.VISIBLE
//        btn_nav_visible.visibility = View.GONE
    }

    //사진 선택
    private fun PhotoSelect() {
        val intent = Intent(this, User_SignUp_PopUp::class.java)
        startActivityForResult(intent, 200)
    }

    //사진 선택 후 돌아오는 데이터 받기
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                imageData =
                    data?.getSerializableExtra("image") as List<com.esafirm.imagepicker.model.Image>
                Toast.makeText(this, "" + imageData, Toast.LENGTH_SHORT).show()
                viewModel.setDataImage(imageData)

                //text_goLogin.setText("" + viewModel.listimage)
                //일단 리스트에 있는 파일 1개를 가져옴 [0]
                val a: RequestBody =
                    RequestBody.create(MediaType.parse("image/jpeg"), viewModel.listimage[0])
                body = MultipartBody.Part.createFormData(
                    "image",
                    (dbemail + ".jpg"), a
                )
                imageretro(body)

            }
        } else {

        }
    }


    fun imageretro(item: MultipartBody.Part) {
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setTitle("업로드중...")
        progressDialog.show()
        Log.d("확인", item.toString())
        RetrofitClient.signupservice.requestImage(item).enqueue(object : Callback<DataImage2> {
            override fun onFailure(call: Call<DataImage2>, t: Throwable) {
                progressDialog.cancel()
                Toast.makeText(
                    applicationContext,
                    "통신 실패",
                    Toast.LENGTH_LONG
                ).show()

            }

            override fun onResponse(call: Call<DataImage2>, response: Response<DataImage2>) {
                progressDialog.cancel()
                Toast.makeText(
                    applicationContext,
                    "변경 완료",
                    Toast.LENGTH_LONG
                ).show()
                val body = response.body()
                Log.d("변경",body?.image)
                val imageurl ="https://118.67.131.138:30000/media/"+body?.image
                Log.d("사용자이미지2",imageurl)
                Glide.with(this@Home).load(imageurl).apply(
                    RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                ).into(main_header_include_logged_in.iv_image);
//                val loginDB = loginDB(context = applicationContext)
//                if (body != null) {
//                    loginDB.alterDB(dbemail.toString(),body.image)
//                }

            }
        })
    }


    //버튼클릭시 애니메이션 효과
    fun homeAnimation(it: Boolean) {
        if (it) {
            val animation: TranslateAnimation = TranslateAnimation(
                0F,
                0F,
                0F,
                50F
            )
            animation.duration = 500
            animation.fillAfter = true
            btn_home_create.startAnimation(animation)
//            code_visible.startAnimation(animation)
//            code_visible.visibility = View.VISIBLE
            its = false

        } else {
            val animation: TranslateAnimation = TranslateAnimation(
                0F,
                0F,
                0F,
                -20F
            )
            animation.duration = 500
            animation.fillAfter = true
            btn_home_create.startAnimation(animation)
//            code_visible.startAnimation(animation)
//            code_visible.visibility = View.GONE
            its = true
        }
    }


    fun deleteUser() {
        //dialog 로 확인메세지 한번 표시해주기

        RetrofitClient.signupservice.requestDelete(dbemail!!).enqueue(object : Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(applicationContext, "회원탈퇴 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.body() == 200) {
                    Toast.makeText(applicationContext, "회원탈퇴 완료되었습니다", Toast.LENGTH_SHORT).show()
                    custom?.dismiss()

                } else {
                    Toast.makeText(applicationContext, "회원탈퇴 실패", Toast.LENGTH_SHORT).show()
                }

            }
        })

    }

}


