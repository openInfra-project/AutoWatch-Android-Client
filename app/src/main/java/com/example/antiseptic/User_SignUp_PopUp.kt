package com.example.antiseptic

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.ImagePickerView

import kotlinx.android.synthetic.main.activity_user__sign_up__pop_up.*
import kotlinx.android.synthetic.main.image_item.*

class User_SignUp_PopUp : AppCompatActivity() {
    private val viewModel: DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__sign_up__pop_up)
        //뒤로가기 버튼 이벤트
        btn_popup_close.setOnClickListener {
            Close()
        }
        //갤러리 클릭 이벤트
        btn_PhotoSelectPopup.setOnClickListener {
            onClickGallary()
        }

    }

    //뒤로가기 버튼
    fun Close() {
        onBackPressed()
    }

    //갤러리 클릭
    fun onClickGallary() {
        //이미지 다중선택
        ImagePicker.create(this)
            .toolbarFolderTitle("폴더")
            .toolbarImageTitle("이미지 선택")
            .toolbarArrowColor(333)
            .limit(10)
            .start()
    }

    //이미지 결과값 받아옴.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            val images = ImagePicker.getImages(data)
            //image 객체를 uri 로 변환해줌
            for (i in 0 until images.size) {
                val item =
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        images[i].id
                    )
                val imagedata = DataImage(Image = item)
                viewModel.setDataImage(imagedata)

            }

            val adapter =
                RecycleAdapter(viewModel.dataImage, LayoutInflater.from(this), onClickDelete = {
                    viewModel.deleteDataImage(it)
                })


            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            viewModel.LiveDataImage.observe(this, Observer {
                viewModel.dataImage
            })
        }
    }


    private inner class RecycleAdapter(
        val itemList: ArrayList<DataImage>,
        val inflater: LayoutInflater,
        val onClickDelete: (dataimage: Int) -> Unit
    ) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val image = view.findViewById<ImageView>(R.id.image_popup_icon)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(inflater.inflate(R.layout.image_item, parent, false))
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.image.setImageURI(itemList[position].Image)

            holder.image.setOnClickListener {
                onClickDelete.invoke(position)
            }
        }
    }
}


