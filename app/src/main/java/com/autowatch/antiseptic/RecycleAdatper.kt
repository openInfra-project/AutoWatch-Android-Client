package com.autowatch.antiseptic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.autowatch.antiseptic.data.DataMyRoomInfo
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapter(
    val itemList: List<DataMyRoomInfo>,
    val inflater: LayoutInflater,
    val onClick: (a: DataMyRoomInfo) -> Unit
    //반환하는건 ViewHolder 객체라고 알고있자.
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    //여기서 viewHolder는 필수 구현요소임.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //여기엔 아이템 객체가 들어있음 !
        val title =view.findViewById<TextView>(R.id.text_title)
        val description = view.findViewById<TextView>(R.id.text_description)
        val mode = view.findViewById<TextView>(R.id.text_mode)
        val date = view.findViewById<TextView>(R.id.text_date)
        val linear = view.findViewById<LinearLayout>(R.id.Linear_recycler)
        val img = view.findViewById<ImageView>(R.id.picture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //여기서 아이템 뷰를 불러와서 뷰 홀더에 넣어줌.
        return ViewHolder(inflater.inflate(R.layout.recycle_item, parent, false))
    }

    override fun getItemCount(): Int {
        //총 아이템 크기
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //날짜 구조 변형

//        val dateTime = itemList[position].fields.make_date
//        val old_dateParser = SimpleDateFormat("EEE MMM d HH:mm:ss z ", Locale.KOREA)
//        val date = old_dateParser.parse(dateTime.toString())
//        val new_dateParser = SimpleDateFormat("EEE MMM d HH:mm:ss", Locale.KOREA)
//        val new_date=new_dateParser.parse(date.toString())

        //아이템 하나에 대한 접근
        holder.mode.setText("" + itemList[position].fields.mode)
        holder.title.setText("" + itemList[position].fields.room_name)
        holder.description.setText(itemList[position].fields.room_password)
        holder.date.setText("" + itemList[position].fields.make_date)
        if (itemList[position].fields.mode=="EXAM"){
            holder.img.setImageResource(R.drawable.exam)
        }else
            holder.img.setImageResource(R.drawable.study)

        holder.linear.setOnClickListener {
            onClick.invoke(itemList[position])
        }

    }
}


