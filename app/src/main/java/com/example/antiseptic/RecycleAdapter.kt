package com.example.antiseptic

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    val itemList: ArrayList<TutorialData>,
    val inflater: LayoutInflater,
    val onClick:(it:Int)->(Unit)
    //반환하는건 ViewHolder 객체라고 알고있자.
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    //여기서 viewHolder는 필수 구현요소임.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //여기엔 아이템 객체가 들어있음 !
        val title =view.findViewById<TextView>(R.id.text_title)
        val description = view.findViewById<TextView>(R.id.text_description)
        val image = view.findViewById<ImageView>(R.id.image_tuto)
        val image2=view.findViewById<ImageView>(R.id.image_tuto2)
        val linear = view.findViewById<LinearLayout>(R.id.linear_tuto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //여기서 아이템 뷰를 불러와서 뷰 홀더에 넣어줌.
        return ViewHolder(inflater.inflate(R.layout.tutorial_item, parent, false))

    }

    override fun getItemCount(): Int {
        //총 아이템 크기
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //아이템 하나에 대한 접근
        holder.title.setText(itemList[position].title)
        holder.description.setText(itemList[position].description)
        holder.image.setImageResource(itemList[position].image)
        holder.image2.setImageResource(itemList[position].image2)
        holder.linear.setOnClickListener {
            onClick.invoke(position)
        }
        holder.image2.setOnClickListener {
            onClick.invoke(position)
        }
    }
}