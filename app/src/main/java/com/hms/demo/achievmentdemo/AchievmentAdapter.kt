package com.hms.demo.achievmentdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huawei.hms.jos.games.achievement.Achievement
import kotlinx.android.synthetic.main.achievment.view.*

class AchievmentAdapter(val list:ArrayList<Achievement>, val listener:MyVH.OnItemClickListener): RecyclerView.Adapter<AchievmentAdapter.MyVH>() {


    class MyVH(private val view: View, val listener: OnItemClickListener): RecyclerView.ViewHolder(view),View.OnClickListener {
        public fun init(achievement: Achievement){
            view.achName.text=achievement.displayName
            view.achDesc.text=achievement.descInfo
            view.achState.text=when(achievement.state){
                3->"Ulocked"
                else ->"Locked"
            }
            view.setOnClickListener(this)

        }
        public interface OnItemClickListener{
            fun onAchievmentClicked(position:Int)
        }

        override fun onClick(v: View?) {
            listener.onAchievmentClicked(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.achievment, parent, false)
        return MyVH(view,listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.init(list[position])
    }
}