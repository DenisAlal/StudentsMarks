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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.denisal.studentsmarks.*
import com.denisal.studentsmarks.auth.SignInActivity
import com.denisal.studentsmarks.settingsactivity.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        val info: FloatingActionButton = view.findViewById(R.id.goInfo)
        info.setOnClickListener{

            val builderSucceed = AlertDialog.Builder(requireContext())

                .setTitle("Информация")
                .setMessage("На этой вкладке можно добавить информацию о студентах, создать предмет, " +
                        "просмотреть списки загруженных студентов и созданных предметов и занятий, " +
                        "очистить данные за текуйщий семестр и выйти из учетной записи, а также сменить " +
                        "цветовое оформление")
                .setIcon(R.drawable.outline_info_24)

            builderSucceed.setPositiveButton("OK"){ _, _ ->
            }
            val alertDialogSuccess: AlertDialog = builderSucceed.create()
            alertDialogSuccess.show()
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