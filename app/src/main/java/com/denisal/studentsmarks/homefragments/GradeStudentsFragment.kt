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
import com.denisal.studentsmarks.scanning.GradeScanningActivity
import com.denisal.studentsmarks.scanning.GradeViewActivity
import com.denisal.studentsmarks.settingsactivity.LoadAndSendStudentsActivity
import com.denisal.studentsmarks.uid


class GradeStudentsFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("uid", uid)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_grade_students, container, false)
        val scanGrade: Button = view.findViewById(R.id.scanGrade)
        scanGrade.setOnClickListener {
            val intent = Intent(activity, GradeScanningActivity::class.java)
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