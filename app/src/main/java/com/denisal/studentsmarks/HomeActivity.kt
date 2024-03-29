package com.denisal.studentsmarks


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.denisal.studentsmarks.databinding.ActivityHomeBinding
import com.denisal.studentsmarks.db.GetFromDB
import com.denisal.studentsmarks.settingsactivity.homefragments.GradeStudentsFragment
import com.denisal.studentsmarks.settingsactivity.homefragments.SettingsFragment
import com.denisal.studentsmarks.settingsactivity.homefragments.AddDataFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.container, GradeStudentsFragment.newInstance()).commit();
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            uid = user.uid
        }
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.gradeStudents -> {
                    openFragment(GradeStudentsFragment.newInstance())
                    return@setOnItemSelectedListener true
                }
                R.id.trafficStudents -> {
                    openFragment(AddDataFragment.newInstance())
                    return@setOnItemSelectedListener true
                }
                R.id.settings -> {
                    openFragment(SettingsFragment.newInstance())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        val getID = GetFromDB()
        getID.getTeacher()
        Log.e("","$teacherID")
    }

    private fun openFragment(fragment: Fragment) {
       
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment).commit()

    }


}