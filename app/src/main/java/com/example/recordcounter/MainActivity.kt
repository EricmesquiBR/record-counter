package com.example.recordcounter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.recordcounter.databinding.ActivityMainBinding
import com.example.recordcounter.ui.about.AboutFragment
import com.example.recordcounter.ui.savedtimes.SavedTimesFragment
import com.example.recordcounter.ui.stopwatch.StopwatchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(StopwatchFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_stopwatch -> loadFragment(StopwatchFragment())
                R.id.nav_saved_times -> loadFragment(SavedTimesFragment())
                R.id.nav_about -> loadFragment(AboutFragment())
                else -> false
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}
