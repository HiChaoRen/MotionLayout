package com.example.motionlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.motionlayout.databinding.ActivityMainBinding
import com.example.motionlayout.demo.DemoActivity1
import com.example.motionlayout.demo.DemoActivity2
import com.example.motionlayout.demo.TestActivity
import com.example.motionlayout.demo.DemoActivityCarousel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.testBtn.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }

        binding.demoBtn1.setOnClickListener {
            startActivity(Intent(this, DemoActivity1::class.java))
        }

        binding.demoBtn2.setOnClickListener {
            startActivity(Intent(this, DemoActivity2::class.java))
        }

        binding.demoBtn4.setOnClickListener {
            startActivity(Intent(this, DemoActivityCarousel::class.java))
        }
    }
}