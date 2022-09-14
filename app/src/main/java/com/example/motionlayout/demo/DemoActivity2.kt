package com.example.motionlayout.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlayout.binding
import com.example.motionlayout.databinding.ActivityDemo2Binding
import com.example.motionlayout.ktx.*
import com.example.motionlayout.ktx.doOnTransitionCompleted
import com.example.motionlayout.ktx.doOnTransitionStarted
import com.example.motionlayout.ktx.doOnTransitionTrigger

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
                var progress = distance / (scrollY * 4)
                if (progress >= 1) {
                    progress = 1f
                }
                binding.motionLayout.progress = progress
            }

            override fun onStopScroll() {
            }
        })

        binding.motionLayout.apply {
            doOnTransitionStarted { motionLayout, startId, endId ->

            }
            doOnTransitionCompleted { motionLayout, currentId ->

            }
            doOnTransitionChange { motionLayout, startId, endId, progress ->

            }
            doOnTransitionTrigger { motionLayout, triggerId, positive, progress ->

            }
        }
    }

}