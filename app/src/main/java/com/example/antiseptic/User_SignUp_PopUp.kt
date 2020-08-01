package com.example.antiseptic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import kotlinx.android.synthetic.main.activity_user__sign_up__pop_up.*
import java.io.Serializable

class User_SignUp_PopUp : AppCompatActivity() {
    private val viewModel: DataViewModel by viewModels()
    //images
    private lateinit var images : List<Image>
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
        btn_PhotoSavePopup.setOnClickListener {
            //이미지 uri 객체를 회원가입 페이지로 보내줌.
            // ViewModel 의 dataImage가 직렬화가 되어야 하는 문제점.
            val intent = Intent()
            val data = images as Serializable
            var bundle =Bundle()
            bundle.putSerializable("image",data)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            //액티비티 팝업 닫기
            finish()



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
            images = ImagePicker.getImages(data)
            //이미지 객체를 바로 ViewModel 으로 보냄.
            viewModel.setDataImage(images)
        }
        //adapter 로 연결해줌
        val adapter =
            RecycleAdapter(viewModel.dataImage, LayoutInflater.from(this), onClickDelete = {
                viewModel.deleteDataImage(it)
            })
        //recyclerView를 연동해주고 Horizontal 로 설정해줌
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
                GridLayoutManager(this,5)
        //이미지데이터 변동사항 체크
        viewModel.LiveDataImage.observe(this, Observer {
            viewModel.dataImage
        })
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




