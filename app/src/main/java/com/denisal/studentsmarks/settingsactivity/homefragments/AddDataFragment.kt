package com.denisal.studentsmarks.settingsactivity.homefragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.scanning.activites.CreateTaskActivity
import com.denisal.studentsmarks.settingsactivity.CreateCourseActivity
import com.denisal.studentsmarks.settingsactivity.TaskViewActivity
import com.denisal.studentsmarks.settingsactivity.LoadAndSendStudentsActivity
import com.denisal.studentsmarks.settingsactivity.SubjectViewActivity
import com.denisal.studentsmarks.settingsactivity.ViewStudentsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddDataFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_add_data, container, false)
        val loadStud: Button = view.findViewById(R.id.loadStudents)
        loadStud.setOnClickListener {
            val intent = Intent(activity, LoadAndSendStudentsActivity::class.java)
            startActivity(intent)
        }
        val createSubject: Button = view.findViewById(R.id.createSub)
        createSubject.setOnClickListener{
            val intent = Intent(activity, CreateCourseActivity::class.java)
            startActivity(intent)
        }

        val gradeTask: Button = view.findViewById(R.id.gradeTask)
        gradeTask.setOnClickListener {
            val intent = Intent(activity, CreateTaskActivity::class.java)
            startActivity(intent)
        }

        val viewStud: Button = view.findViewById(R.id.viewStud)
        viewStud.setOnClickListener {
            val intent = Intent(activity, ViewStudentsActivity::class.java)
            startActivity(intent)
        }
        val viewCourse: Button = view.findViewById(R.id.viewCourse)
        viewCourse.setOnClickListener {
            val intent = Intent(activity, SubjectViewActivity::class.java)
            startActivity(intent)
        }
        val viewLesson: Button = view.findViewById(R.id.viewTask)
        viewLesson.setOnClickListener {
            val intent = Intent(activity, TaskViewActivity::class.java)
            startActivity(intent)
        }
        val info: FloatingActionButton = view.findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(requireContext())
                .setTitle("Информация")
                .setMessage("На этой вкладке можно добавить информацию о студентах, создать предмет, " +
                    "просмотреть списки загруженных студентов и созданных предметов и занятий)")
                .setIcon(R.drawable.outline_info_24)
            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddDataFragment().apply {}
    }
}