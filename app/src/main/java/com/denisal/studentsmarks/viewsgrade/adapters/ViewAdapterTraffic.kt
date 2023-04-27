package com.denisal.studentsmarks.viewsgrade.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.viewsgrade.TrafficViewActivity
import com.denisal.studentsmarks.viewsgrade.viewmodels.TrafficDataView

class ViewAdapterTraffic(private val list: List<TrafficDataView>, val listener: TrafficViewActivity):
    RecyclerView.Adapter<ViewAdapterTraffic.RowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.table_list_item_traffic, parent, false)
        return RowViewHolder(itemView)
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val rowPos = holder.bindingAdapterPosition

        if (rowPos == 0) {
            holder.apply {
                setHeaderBg(date)
                setHeaderBg(courseName)
                setHeaderBg(lessonName)
                setHeaderBg(fio)
                courseName.text = "Предмет"
                lessonName.text = "Занятие"
                date.text = "Дата"
                fio.text = "ФИО"

            }
        } else {
            val modal = list[rowPos - 1]

            holder.apply {
                setContentBg(courseName)
                setContentBg(lessonName)
                setContentBg(date)
                setContentBg(fio)
                courseName.text = modal.courseName
                lessonName.text = modal.lessonName
                date.text = modal.date
                fio.text = modal.fio

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1 // one more to add header row
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val courseName: TextView = itemView.findViewById(R.id.courseName)
        val lessonName: TextView = itemView.findViewById(R.id.lessonName)
        val fio: TextView = itemView.findViewById(R.id.fio)
        init {
            itemView.setOnClickListener{
                val position = adapterPosition
                listener.onClick(position)
            }
        }
    }
}