package com.denisal.studentsmarks.homefragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.scanning.activites.ChooseTaskGradeActivity
import com.denisal.studentsmarks.scanning.activites.CreateSubjectActivity
import com.denisal.studentsmarks.scanning.activites.CreateTaskActivity
import com.denisal.studentsmarks.scanning.activites.GradeViewActivity
import com.denisal.studentsmarks.uid


class GradeStudentsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("uid", uid)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_grade_students, container, false)
        val createSubject: Button = view.findViewById(R.id.createSubject)
        createSubject.setOnClickListener {
            val intent = Intent(activity, CreateSubjectActivity::class.java)
            startActivity(intent)
        }
        val gradeTask: Button = view.findViewById(R.id.gradeTask)
        gradeTask.setOnClickListener {
            val intent = Intent(activity, CreateTaskActivity::class.java)
            startActivity(intent)
        }
        val gradeScan: Button = view.findViewById(R.id.gradeScan)
        gradeScan.setOnClickListener {
            val intent = Intent(activity, ChooseTaskGradeActivity::class.java)
            startActivity(intent)
        }
        val gradeView: Button = view.findViewById(R.id.gradeView)
        gradeView.setOnClickListener {
            val intent = Intent(activity, GradeViewActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = GradeStudentsFragment().apply {}
    }
}