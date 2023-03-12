package com.denisal.studentsmarks


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.denisal.studentsmarks.databinding.ActivityHomeBinding
import com.denisal.studentsmarks.homefragments.GradeStudentsFragment
import com.denisal.studentsmarks.homefragments.SettingsFragment
import com.denisal.studentsmarks.homefragments.TrafficStudentsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    lateinit var bottomNavigationView: BottomNavigationView
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
                /*
                R.id.QRCode -> {
                    openFragment(QrCodeFragment.newInstance());
                    return@setOnItemSelectedListener true
                }*/
                R.id.gradeStudents -> {
                    openFragment(GradeStudentsFragment.newInstance())
                    return@setOnItemSelectedListener true
                }
                R.id.trafficStudents -> {
                    openFragment(TrafficStudentsFragment.newInstance())
                    return@setOnItemSelectedListener true
                }
                R.id.settings -> {
                    openFragment(SettingsFragment.newInstance())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

}