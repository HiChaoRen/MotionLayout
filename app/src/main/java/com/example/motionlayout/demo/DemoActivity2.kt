package com.example.motionlayout.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlayout.UiUtils
import com.example.motionlayout.binding
import com.example.motionlayout.databinding.ActivityDemo2Binding

class DemoActivity2 : AppCompatActivity() {

    private val binding: ActivityDemo2Binding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.nestedScrollView.setScrollStateListener(object : StateKnowNestedScrollView.ScrollStateListener {
            override fun onStartScroll(dy: Float) {
                val scrollY = UiUtils.dp2px(this@DemoActivity2, 38f)
                var distance = dy - UiUtils.dp2px(this@DemoActivity2, 250f)
                if (distance <= 0) {
                    distance = 0f
                }
                val progress = distance / (scrollY * 4)
                binding.toolContent.progress = progress
            }

            override fun onStopScroll() {
            }
        })

    }

}