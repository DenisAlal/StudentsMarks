package com.denisal.studentsmarks.settingsactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.R

class StudentsAdapter(private  val mList :List<StudentsViewModel>):RecyclerView.Adapter<StudentsAdapter.ViewHolder>() {

    override fun   onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentsViewModel = mList[position]

        holder.tvFIO.text = studentsViewModel.fio
        holder.tvGroup.text = studentsViewModel.group
    }
    override fun getItemCount(): Int {
        return  mList.size
    }
    class ViewHolder(itemView :View):RecyclerView.ViewHolder(itemView){
        val tvFIO :TextView = itemView.findViewById(R.id.tvFIO)
        val tvGroup : TextView = itemView.findViewById(R.id.tvGroup)
    }


}



