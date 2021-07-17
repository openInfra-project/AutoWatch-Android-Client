package com.autowatch.antiseptic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.autowatch.antiseptic.data.DataMyRoomInfo

class RecyclerAdapter(
    val itemList: List<DataMyRoomInfo>,
    val inflater: LayoutInflater,
    val onClick:(a:DataMyRoomInfo) ->Unit
    //반환하는건 ViewHolder 객체라고 알고있자.
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    //여기서 viewHolder는 필수 구현요소임.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //여기엔 아이템 객체가 들어있음 !
        val title =view.findViewById<TextView>(R.id.text_title)
        val description = view.findViewById<TextView>(R.id.text_description)
        val mode = view.findViewById<TextView>(R.id.text_mode)
        val linear = view.findViewById<LinearLayout>(R.id.Linear_recycler)
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
        //아이템 하나에 대한 접근
        holder.title.setText(""+itemList[position].fields.room_name)
        holder.description.setText(itemList[position].fields.room_ps)
        holder.mode.setText(itemList[position].fields.room_func)
        holder.linear.setOnClickListener {
            onClick.invoke(itemList[position])
        }

    }
}