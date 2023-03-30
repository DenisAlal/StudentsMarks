package com.denisal.studentsmarks.settingsactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denisal.studentsmarks.R

class SubjectsAdapter(private val mList:List<ViewModelSubjects>, val listener: SubjectViewActivity)
    :RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {
    override fun   onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subject_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subjViewModel = mList[position]
        holder.tvID.text = subjViewModel.id.toString()
        holder.tvName.text = subjViewModel.name
        holder.tvLection.text = subjViewModel.lecture
        holder.tvPractic.text = subjViewModel.practic
    }
    override fun getItemCount(): Int {
        return  mList.size
    }
    inner class ViewHolder(itemView :View):RecyclerView.ViewHolder(itemView){
        val tvID :TextView = itemView.findViewById(R.id.tvID)
        val tvName :TextView = itemView.findViewById(R.id.tvName)
        val tvLection :TextView = itemView.findViewById(R.id.tvLection)
        val tvPractic :TextView = itemView.findViewById(R.id.tvPractic)
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



