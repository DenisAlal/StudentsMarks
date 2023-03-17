package com.denisal.studentsmarks.homefragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.auth.SignInActivity
import com.denisal.studentsmarks.settingsactivity.DeleteInfoActivity
import com.denisal.studentsmarks.settingsactivity.LoadAndSendStudentsActivity
import com.denisal.studentsmarks.settingsactivity.CreateCourseActivity
import com.denisal.studentsmarks.settingsactivity.ViewStudentsActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)
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
        val viewStud: Button = view.findViewById(R.id.viewStud)
        viewStud.setOnClickListener {
            val intent = Intent(activity, ViewStudentsActivity::class.java)
            startActivity(intent)
        }
        val deleteInformation: Button = view.findViewById(R.id.delInfo)
        deleteInformation.setOnClickListener {
            val intent = Intent(activity, DeleteInfoActivity::class.java)
            startActivity(intent)
        }
        val logOut: Button = view.findViewById(R.id.logOut)
        logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return view
    }
    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment().apply{}
    }
}