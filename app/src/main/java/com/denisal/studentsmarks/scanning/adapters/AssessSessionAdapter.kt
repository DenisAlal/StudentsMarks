package com.denisal.studentsmarks.scanning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.db.assessments.AssesDataRoom
import com.denisal.studentsmarks.scanning.activites.AddAssessmentsActivity

class AssessSessionAdapter(private val mList: MutableList<AssesDataRoom>, val listener: AddAssessmentsActivity)
    :RecyclerView.Adapter<AssessSessionAdapter.ViewHolder>() {
    override fun   onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mark_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewModel = mList[position]
        holder.tvID.text = viewModel.id.toString()
        holder.tvTask.text = viewModel.taskName
        holder.tvMark.text = viewModel.value
    }
    override fun getItemCount(): Int {
        return  mList.size
    }
    inner class ViewHolder(itemView :View):RecyclerView.ViewHolder(itemView){
        val tvID :TextView = itemView.findViewById(R.id.tvID)
        val tvTask :TextView = itemView.findViewById(R.id.tvTask)
        val tvMark : TextView = itemView.findViewById(R.id.tvMark)
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



