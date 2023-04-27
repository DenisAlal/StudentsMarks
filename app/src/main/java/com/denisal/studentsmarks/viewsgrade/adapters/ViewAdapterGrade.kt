package com.denisal.studentsmarks.viewsgrade.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.viewsgrade.GradeViewActivity
import com.denisal.studentsmarks.viewsgrade.viewmodels.GradeDataView

class ViewAdapterGrade(private val list: MutableList<GradeDataView>, val listener: GradeViewActivity):
    RecyclerView.Adapter<ViewAdapterGrade.RowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.table_list_item_grade, parent, false)
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
                setHeaderBg(courseName)
                setHeaderBg(lessonName)
                setHeaderBg(taskName)
                setHeaderBg(group)
                setHeaderBg(fio)
                setHeaderBg(date)
                setHeaderBg(value)
                courseName.text = "Предмет"
                lessonName.text = "Занятие"
                taskName.text = "Задание"
                group.text =" Группа"
                fio.text = " ФИО"
                date.text = "Дата сдачи"
                value.text = "Оценка"

            }
        } else {
            val modal = list[rowPos - 1]

            holder.apply {
                setContentBg(courseName)
                setContentBg(lessonName)
                setContentBg(taskName)
                setContentBg(group)
                setContentBg(fio)
                setContentBg(date)
                setContentBg(value)
                courseName.text = modal.courseName
                lessonName.text = modal.lessonName
                taskName.text = modal.taskName
                group.text = modal.group
                fio.text = modal.fio
                date.text = modal.date
                value.text = modal.value

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.courseNameGrade)
        val lessonName: TextView = itemView.findViewById(R.id.lessonNameGrade)
        val taskName: TextView = itemView.findViewById(R.id.taskNameGrade)
        val group: TextView = itemView.findViewById(R.id.groupGrade)
        val fio: TextView = itemView.findViewById(R.id.fioGrade)
        val date: TextView = itemView.findViewById(R.id.dateGrade)
        val value: TextView = itemView.findViewById(R.id.valueGrade)
        init {
            itemView.setOnClickListener{
                val position = adapterPosition
                listener.onClick(position)
            }
        }
    }
}