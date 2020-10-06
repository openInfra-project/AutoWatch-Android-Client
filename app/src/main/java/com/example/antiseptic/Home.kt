package com.example.antiseptic

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.antiseptic.data.DataImage2
import com.example.antiseptic.data.DataRoomNumber
import com.example.antiseptic.data.DataViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.drawer_view
import kotlinx.android.synthetic.main.activity_manager_room_pop_up.*
import kotlinx.android.synthetic.main.nav_header.*
import me.piruin.quickaction.ActionItem
import me.piruin.quickaction.QuickAction
import me.piruin.quickaction.QuickIntentAction
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
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val loginDB = loginDB(context = applicationContext)
        code_visible.visibility = View.GONE
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
            dbpassword = dbjson.getString("password") ?: null
            dbname = dbjson.getString("name") ?: null
        } else {

        }


        if (dbemail != null && dbpassword != null && dbname != null) {
            login()
        } else {
            logout()
        }
        //테스트
        testtest.setOnClickListener {
            startActivity(Intent(this,EnterMyRoom::class.java))
        }


        //뒤로가기 버튼
        //방입장 버튼 클릭시
        btn_home_join.setOnClickListener {
            homeAnimation(it = its)
        }
        btn_home_inner.setOnClickListener {
            if (edit_join_roomname.text.toString() != null && edit_join_password.text.toString() != null) {
                enterroom(edit_join_roomname.text.toString(), edit_join_password.text.toString())
            }
        }
        //도움말 버튼 애니메이션
        val animation = AnimationUtils.loadAnimation(this, R.anim.home_highlight)
        image_home_highlight.startAnimation(animation)
        val animationbackground =
            AnimationUtils.loadAnimation(this, R.anim.home_highlight_background)
        Linear_background.startAnimation(animationbackground)
        //도움말 Dialog 여기서 구현.
        //menu 애니메이션
        btn_home_menu.setOnClickListener {
            drawer_view.openDrawer(nav_view)
        }
        btn_nav_close.setOnClickListener {
            drawer_view.closeDrawers()
        }
        //방목록
        btn_nav_room.setOnClickListener {
            startActivity(Intent(this, NavRoom::class.java))
        }
        //nav 학생정보입력
        btn_nav_info.setOnClickListener {
            startActivity(Intent(this, UserInfo::class.java))
        }
        //로그아웃
        btn_nav_logout.setOnClickListener {
            loginDB.deleteDB()
            logout()
            startActivity(Intent(this, Home::class.java))
        }
        //로그인하러 가기
        btn_nav_visible.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        //회원탈퇴
        btn_home_deleteuser.setOnClickListener {
            val custom = DeleteDialog(context = this)
            custom.show()
        }


        //deleteUser()


        drawer_view.setOnTouchListener { v, event -> true }


        frame_highlight.setOnClickListener {
            quickActivity()
        }
        //사진선택기능
        btn_home_changeimage.setOnClickListener {
            PhotoSelect()
        }
        btn_home_create.setOnClickListener {
            startActivity(Intent(this, ManagerRoomPopUp::class.java))
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
                    //1번이면 얼굴인식 후 방입장
                    //2번이면 바로 방입장
                    //3번이면 방이 없음.
                    if(body!=null) {
                        btn_home_inner.setText(""+body.roomname)
                        if(body.roomname=="1") {
                            Toast.makeText(
                                applicationContext,
                                "얼굴 인식 페이지로 이동합니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(applicationContext, Room::class.java))
                        }else if(body.roomname=="2"){
                            //바로 방입장
                            Toast.makeText(
                                applicationContext,
                                "방 입장합니다",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(body.roomname=="None") {
                            Toast.makeText(
                                applicationContext,
                                "방이 존재하지 않거나 비번이 틀립니다.",
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
        text_nav_login.setText("로그인하러가기")
        text_nav_name.visibility = View.GONE
        linear_nav_login.visibility = View.GONE
        btn_nav_visible.visibility = View.VISIBLE
    }

    fun login() {
        //로그인시 화면
        text_nav_login.setText("환영합니다")
        text_nav_name.setText("" + dbname + "님")
        //로그아웃 시 다시 Home 으로 이동 후 바뀐 nav 보여줌
        linear_nav_login.visibility = View.VISIBLE
        btn_nav_visible.visibility = View.GONE
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
            code_visible.startAnimation(animation)
            code_visible.visibility = View.VISIBLE
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
            code_visible.startAnimation(animation)
            code_visible.visibility = View.GONE
            its = true
        }
    }


    fun quickActivity() {
        QuickAction?.setColor(999)
        QuickAction?.setTextColor(333)
        val item = ActionItem(1, "튜토리얼을 뭘적지 ??")
        val quickAction = QuickAction(this, me.piruin.quickaction.QuickAction.VERTICAL)
        quickAction.setColorRes(R.color.colorPrimary)
        quickAction.setTextColorRes(R.color.colorAccent)
        quickAction.addActionItem(item)
        quickAction.setOnActionItemClickListener(object : QuickAction.OnActionItemClickListener {
            override fun onItemClick(item: ActionItem?) {

            }
        })
        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)
        val quickIntent = QuickIntentAction(this)
            .setActivityIntent(intent)
            .create()
        quickIntent.setAnimStyle(me.piruin.quickaction.QuickAction.Animation.GROW_FROM_CENTER)
        quickAction.show(frame_highlight)
    }


}


