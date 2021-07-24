package com.autowatch.antiseptic

import android.animation.Animator
import android.app.Activity
import android.app.ProgressDialog
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
    var btn_again: Button? = null
    var room_sendlottie :Button? =null
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

        // findViewById
        sv_viewFinder = findViewById<View>(R.id.sv_viewFinder) as SurfaceView
        sh_viewFinder = sv_viewFinder!!.holder
        sh_viewFinder?.addCallback(surfaceListener)
        sh_viewFinder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        btn_shutter = findViewById<View>(R.id.btn_shutter) as Button
        room_sendlottie = findViewById<View>(R.id.room_sendlottie) as Button
        btn_again = findViewById<View>(R.id.btn_again) as Button

        iv_preview = findViewById<View>(R.id.iv_preview) as ImageView
        //카운트 애니메이션 끝날때 할 작업
        room_countlottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {

                startTakePicture()
                room_countlottie.visibility = View.GONE
                btn_again?.setVisibility(View.VISIBLE);
                room_sendlottie?.setVisibility(View.VISIBLE);


            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
                btn_shutter?.setVisibility(View.GONE);
                btn_again?.setVisibility(View.GONE);
                room_sendlottie?.setVisibility(View.GONE);


            }
        })
        //뒤로가기
        btn_room_backpress.setOnClickListener {
            onBackPressed()
        }

        btn_again!!.setOnClickListener {
            camera!!.startPreview()
            btn_shutter?.setVisibility(View.VISIBLE);
            btn_again?.setVisibility(View.GONE);
            room_sendlottie?.setVisibility(View.GONE);
        }
        //이미지 서버로 전송
        room_sendlottie?.setOnClickListener {

            if (myfile != null) {
                room_secondrocket_lottie.visibility = View.VISIBLE
                val a: RequestBody =
                    RequestBody.create(MediaType.parse("image/jpeg"), myfile)
                body = MultipartBody.Part.createFormData(
                    "image",
                    (dbemail + ".jpg"), a
                )
                goimage(body)
                Log.d("사진전송", myfile.toString())
                Log.d("사진전송", body.toString())


            } else {
                Toast.makeText(this, "이미지를 찍어주세요", Toast.LENGTH_SHORT).show()
            }
        }



        // setListener
        btn_shutter!!.setOnClickListener(
            onClickListener_btn_shutter)


    }

    fun goimage(item: MultipartBody.Part) {
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setTitle("본인 확인 중...")
        progressDialog.show()
        Log.d("확인", item.toString())
        RetrofitClient.signupservice.myrequestImage2(item).enqueue(object : Callback<DataImage2> {
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
                    "본인 확인 완료",
                    Toast.LENGTH_LONG
                ).show()
                val body = response.body()
                Log.d("사진 본인확인",body?.image)

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
            camera!!.setDisplayOrientation(90)

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
        View.OnClickListener {
            btn_shutter?.setVisibility(View.GONE);
            btn_again?.setVisibility(View.VISIBLE);
            room_sendlottie?.setVisibility(View.VISIBLE);
            startTakePicture() }
    var takePicture = PictureCallback { data, camera ->
        Log.d("1", "=== takePicture ===")
        if (data != null) {
            Log.v("1", "takePicture JPEG 사진 찍음")
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            iv_preview!!.setImageBitmap(bitmap)
            iv_preview!!.setVisibility(View.GONE);

            camera.startPreview()
            inProgress = false
            bytearraytoFile(data)
            camera.stopPreview()

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
