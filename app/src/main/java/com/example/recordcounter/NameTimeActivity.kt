package com.example.recordcounter
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.example.recordcounter.databinding.ActivityNameTimeBinding
//
//class NameTimeActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityNameTimeBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityNameTimeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val time = intent.getDoubleExtra("TIME_EXTRA", 0.0)
//
//        binding.btnSave.setOnClickListener {
//            val name = binding.etName.text.toString()
//            // Save time and name to the database here
//            // For now, show a toast message
//            showToast("Saved time is ${getTimeStringFromDouble(time)} with name $name")
//            finish() // Return to main activity
//        }
//    }
//
//    private fun getTimeStringFromDouble(time: Double): String {
//        val resultInt = time.toInt()
//        val hours = resultInt / 3600
//        val minutes = (resultInt % 3600) / 60
//        val seconds = resultInt % 60
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}
