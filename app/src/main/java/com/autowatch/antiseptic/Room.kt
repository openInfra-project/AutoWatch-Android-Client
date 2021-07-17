package com.autowatch.antiseptic

import android.animation.Animator
import android.app.Activity
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.PictureCallback
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.autowatch.antiseptic.data.DataImage2
import kotlinx.android.synthetic.main.activity_room.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Room : Activity() {
    var sv_viewFinder: SurfaceView? = null
    var sh_viewFinder: SurfaceHolder? = null
    var camera: Camera? = null
    var myfile:File?=null
    var btn_shutter: Button? = null
    var iv_preview: ImageView? = null
    var fos: FileOutputStream? = null
    private var dbemail: String? = null
    private lateinit var body: MultipartBody.Part
    var inProgress = false
    var myimage: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        val loginDB = loginDB(context = applicationContext)
        val dbjson: JSONObject = loginDB.getloginDB()
        if (dbjson.length() > 0) {
            dbemail = dbjson.getString("email") ?: null
        }
        //카운트 애니메이션 끝날때 할 작업
        room_countlottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                room_countlottie.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        //뒤로가기
        btn_room_backpress.setOnClickListener {
            onBackPressed()
        }
        //이미지 서버로 전송
        room_sendlottie.setOnClickListener {

            if (myfile != null) {
                room_secondrocket_lottie.visibility = View.VISIBLE
                val a: RequestBody =
                    RequestBody.create(MediaType.parse("image/jpeg"), myfile)
                body = MultipartBody.Part.createFormData(
                    "image",
                    (dbemail + ".jpg"), a
                )
                goimage(body)


            } else {
                Toast.makeText(this, "이미지를 찍어주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // findViewById
        sv_viewFinder = findViewById<View>(R.id.sv_viewFinder) as SurfaceView
        sh_viewFinder = sv_viewFinder!!.holder
        sh_viewFinder?.addCallback(surfaceListener)
        sh_viewFinder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        btn_shutter = findViewById<View>(R.id.btn_shutter) as Button
        iv_preview = findViewById<View>(R.id.iv_preview) as ImageView

        // setListener
        btn_shutter!!.setOnClickListener(onClickListener_btn_shutter)

        // 3초 뒤 자동촬영
        val timer = Timer()
        val tt: TimerTask = object : TimerTask() {
            override fun run() {
                startTakePicture()
            }
        }
        timer.schedule(tt, 3000)
    }

    fun goimage(item: MultipartBody.Part) {
        Toast.makeText(this, "" + item, Toast.LENGTH_LONG).show()
        RetrofitClient.signupservice.myrequestImage2(item).enqueue(object :
            Callback<DataImage2> {
            override fun onFailure(call: Call<DataImage2>, t: Throwable) {
                room_secondrocket_lottie.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    "인증이 실패하였습니다 다시 전송바랍니다." + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<DataImage2>, response: Response<DataImage2>) {
                room_secondrocket_lottie.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    "인증 성공 방으로 입장합니다",
                    Toast.LENGTH_LONG
                ).show()

            }
        })
    }

    var surfaceListener: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
            Log.i("1", "sufraceListener 카메라 미리보기 활성")
            val parameters = camera!!.parameters
            parameters.setPreviewSize(width, height)
            camera!!.startPreview()
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.i("1", "sufraceListener 카메라 오픈")
            var int_cameraID = 0
            /* 카메라가 여러개 일 경우 그 수를 가져옴  */
            val numberOfCameras = Camera.getNumberOfCameras()
            val cameraInfo = CameraInfo()
            for (i in 0 until numberOfCameras) {
                Camera.getCameraInfo(i, cameraInfo)

                // 전면카메라
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT)
                    int_cameraID = i;
                // 후면카메라
                //if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) int_cameraID = i
            }
            camera = Camera.open(int_cameraID)
            try {
                camera?.setPreviewDisplay(sh_viewFinder)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i("1", "sufraceListener 카메라 해제")
            camera!!.release()
            camera = null
        }
    }

    fun startTakePicture() {
        if (camera != null && inProgress == false) {
            camera!!.takePicture(null, null, takePicture)
            inProgress = true
        }
    }

    var onClickListener_btn_shutter =
        View.OnClickListener { startTakePicture() }
    var takePicture = PictureCallback { data, camera ->
        Log.d("1", "=== takePicture ===")
        if (data != null) {
            Log.v("1", "takePicture JPEG 사진 찍음")
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            iv_preview!!.setImageBitmap(bitmap)
            camera.startPreview()
            inProgress = false
            bytearraytoFile(data)

        } else {
            Log.e("1", "takePicture data null")
        }
    }
    fun bytearraytoFile(data:ByteArray) {
        myfile = File(applicationContext.getCacheDir(), "image")
        myfile?.createNewFile()
        try {
            fos = FileOutputStream(myfile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(data)
            fos!!.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}


