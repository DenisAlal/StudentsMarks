package com.denisal.studentsmarks.homefragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.scanning.CreateSubjectActivity
import com.denisal.studentsmarks.scanning.TrafficViewActivity

class TrafficStudentsFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_traffic_students, container, false)
        val createSubTraffic: Button = view.findViewById(R.id.createSubTraffic)
        createSubTraffic.setOnClickListener {
            val intent = Intent(activity, CreateSubjectActivity::class.java)
            startActivity(intent)
        }
        val trafdicScan: Button = view.findViewById(R.id.trafficScan)
        trafdicScan.setOnClickListener {
            val intent = Intent(activity, CreateSubjectActivity::class.java)
            startActivity(intent)
        }
        val trafficView: Button = view.findViewById(R.id.trafficView)
        trafficView.setOnClickListener {
            val intent = Intent(activity, TrafficViewActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrafficStudentsFragment().apply {}
    }
}