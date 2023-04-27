package com.denisal.studentsmarks.settingsactivity.homefragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.scanning.activites.AddAccountingActivity
import com.denisal.studentsmarks.uid
import com.denisal.studentsmarks.viewsgrade.TrafficViewActivity
import com.denisal.studentsmarks.viewsgrade.GradeViewActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


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

        val gradeScan: Button = view.findViewById(R.id.gradeScan)
        gradeScan.setOnClickListener {
            val intent = Intent(activity, AddAccountingActivity::class.java)
            startActivity(intent)
        }
        val gradeView: Button = view.findViewById(R.id.gradeView)
        gradeView.setOnClickListener {
            val intent = Intent(activity, GradeViewActivity::class.java)
            startActivity(intent)
        }
        val trafficView: Button = view.findViewById(R.id.trafficView)
        trafficView.setOnClickListener {
            val intent = Intent(activity, TrafficViewActivity::class.java)
            startActivity(intent)
        }

        val info: FloatingActionButton = view.findViewById(R.id.goInfo)
        info.setOnClickListener{
            val builderSucceed = AlertDialog.Builder(requireContext())
                .setTitle("Информация")
                .setMessage("На этой вкладке можно просмотреть успеваемость обучающихся, и добавить информацию о успеваемости")
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
        fun newInstance() = GradeStudentsFragment().apply {}
    }
}