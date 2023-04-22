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
import com.denisal.studentsmarks.scanning.activites.ChooseLessonTrafficActivity
import com.denisal.studentsmarks.scanning.activites.AddAccountingActivity
import com.denisal.studentsmarks.scanning.activites.TrafficViewActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TrafficStudentsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_traffic_students, container, false)
        val createSubTraffic: Button = view.findViewById(R.id.createSubTraffic)
        createSubTraffic.setOnClickListener {
            val intent = Intent(activity, AddAccountingActivity::class.java)
            startActivity(intent)
        }

        val trafficScan: Button = view.findViewById(R.id.trafficScan)
        trafficScan.setOnClickListener {
            val intent = Intent(activity, ChooseLessonTrafficActivity::class.java)
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
                .setMessage("На этой вкладке можно просмотреть посещаемость " +
                        "обучающихся, и добавить информацию о посещаемости")
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
        fun newInstance() = TrafficStudentsFragment().apply {}
    }
}