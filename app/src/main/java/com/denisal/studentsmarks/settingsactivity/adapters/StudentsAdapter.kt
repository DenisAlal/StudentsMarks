package com.denisal.studentsmarks.settingsactivity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.settingsactivity.ViewStudentsActivity
import com.denisal.studentsmarks.settingsactivity.viewModels.ViewModelStudents

class StudentsAdapter(private val mList:List<ViewModelStudents>, val listener: ViewStudentsActivity)
    :RecyclerView.Adapter<StudentsAdapter.ViewHolder>() {
    override fun   onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentsViewModel = mList[position]
        holder.tvid.text = studentsViewModel.id.toString()
        holder.tvFIO.text = studentsViewModel.fio
        holder.tvGroup.text = studentsViewModel.group
    }
    override fun getItemCount(): Int {
        return  mList.size
    }
    inner class ViewHolder(itemView :View):RecyclerView.ViewHolder(itemView){
        val tvid :TextView = itemView.findViewById(R.id.tvid)
        val tvFIO :TextView = itemView.findViewById(R.id.tvFIO)
        val tvGroup : TextView = itemView.findViewById(R.id.tvGroup)
        init {
            itemView.setOnClickListener{
                val position = adapterPosition
                listener.onClick(position)
            }
        }
    }
    interface MyClickListener {
        fun onClick(position: Int)
    }
}



