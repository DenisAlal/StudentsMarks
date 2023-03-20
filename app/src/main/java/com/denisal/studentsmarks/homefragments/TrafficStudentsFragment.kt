package com.denisal.studentsmarks.homefragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.denisal.studentsmarks.R
import com.denisal.studentsmarks.scanning.*
import com.google.zxing.qrcode.encoder.QRCode

class TrafficStudentsFragment : Fragment() {


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

        val trafficScan: Button = view.findViewById(R.id.trafficScan)
        trafficScan.setOnClickListener {
            val intent = Intent(activity, QrTrafficActivity::class.java)
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