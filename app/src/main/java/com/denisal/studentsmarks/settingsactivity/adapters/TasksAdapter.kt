package com.denisal.studentsmarks.settingsactivity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.settingsactivity.TaskViewActivity
import com.denisal.studentsmarks.settingsactivity.viewModels.ViewModelTasks

class TasksAdapter(private val mList:List<ViewModelTasks>, val listener: TaskViewActivity)
    :RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    override fun   onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentsViewModel = mList[position]
        holder.tvID.text = studentsViewModel.id.toString()
        holder.tvNameCourse.text = studentsViewModel.courseName
        holder.tvNameTask.text = studentsViewModel.name


    }
    override fun getItemCount(): Int {
        return  mList.size
    }
    inner class ViewHolder(itemView :View):RecyclerView.ViewHolder(itemView){
        val tvID :TextView = itemView.findViewById(R.id.tvID)
        val tvNameCourse :TextView = itemView.findViewById(R.id.tvNameCourse)
        val tvNameTask: TextView = itemView.findViewById(R.id.tvNameTask)


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



