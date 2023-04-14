package com.denisal.studentsmarks.settingsactivity.homefragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.auth.SignInActivity
import com.denisal.studentsmarks.settingsactivity.*
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.processphoenix.ProcessPhoenix

class SettingsFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences
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
        val viewCourse: Button = view.findViewById(R.id.viewCourse)
        viewCourse.setOnClickListener {
            val intent = Intent(activity, SubjectViewActivity::class.java)
            startActivity(intent)
        }
        val viewLesson: Button = view.findViewById(R.id.viewLesson)
        viewLesson.setOnClickListener {
            val intent = Intent(activity, LessonViewActivity::class.java)
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

        val theme: SwitchCompat = view.findViewById(R.id.theme)
        sharedPref = activity?.getSharedPreferences("THEME", Context.MODE_PRIVATE)!!
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val nightMode = sharedPref.getBoolean("nightMode", false)
        if (nightMode) {
            theme.isChecked = true
        }
        theme.setOnClickListener{
            if (theme.isChecked) {
                editor.putBoolean("nightMode", true).apply()
                ProcessPhoenix.triggerRebirth(context)
            } else {
                editor.putBoolean("nightMode", false).apply()
                ProcessPhoenix.triggerRebirth(context)
            }
        }

        return view
    }
    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment().apply{}
    }
}