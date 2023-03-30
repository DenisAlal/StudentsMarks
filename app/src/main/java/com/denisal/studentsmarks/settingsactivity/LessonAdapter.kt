package com.denisal.studentsmarks.settingsactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.R

class LessonAdapter(private val mList:List<ViewModelLessons>, val listener: LessonViewActivity)
    :RecyclerView.Adapter<LessonAdapter.ViewHolder>() {
    override fun   onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentsViewModel = mList[position]
        holder.tvIDLesson.text = studentsViewModel.id.toString()
        holder.tvNameLesson.text = studentsViewModel.name
        holder.tvDate.text = studentsViewModel.date
        holder.tvTime.text = studentsViewModel.time
        holder.tvLessonType.text = studentsViewModel.lessonType

    }
    override fun getItemCount(): Int {
        return  mList.size
    }
    inner class ViewHolder(itemView :View):RecyclerView.ViewHolder(itemView){
        val tvIDLesson :TextView = itemView.findViewById(R.id.tvIDLesson)
        val tvNameLesson :TextView = itemView.findViewById(R.id.tvNameLesson)
        val tvDate :TextView = itemView.findViewById(R.id.tvDate)
        val tvTime :TextView = itemView.findViewById(R.id.tvTime)
        val tvLessonType :TextView = itemView.findViewById(R.id.tvLessonType)

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



