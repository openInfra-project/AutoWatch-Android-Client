package com.example.antiseptic

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_user__sign_up__pop_up.*

class User_SignUp_PopUp : AppCompatActivity() {
    private val viewModel : DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__sign_up__pop_up)
        //뒤로가기 버튼
        btn_popup_close.setOnClickListener {
            Close()
        }

    }

    //뒤로가기 버튼
    fun Close() {
        onBackPressed()
    }
    //갤러리 클릭
    fun onClickGallary() {
        //이미지 다중선택
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        intent.setType("image/*")
        startActivityForResult(Intent.createChooser(intent,"사진을 선택하세요"),200)

    }
    //이미지 결과값 받아옴.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 200) {
            if(resultCode == Activity.RESULT_OK) {
                //여기서 이미지를 DataImage 객체에 넣고
                //DataImage 객체를 Livedata에 집어넣어서 그 객체를 RecycleAdapter로 보내야함
                val uri = data?.data
                val clipData =data?.clipData
                if(clipData!=null) {
                    for(i in 0 until 5){
                        viewModel.setDataImage(clipData.getItemAt(i).uri)
                    }
                }
            }
        }
    }

    private inner class RecycleAdapter(
        val itemList: ArrayList<DataImage>,
        val inflater: LayoutInflater
    ) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val image = view.findViewById<ImageView>(R.id.image_popup_icon)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(inflater.inflate(R.layout.image_item,parent,false))
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.image.setImageURI(itemList[position].Image)
        }
    }
}
