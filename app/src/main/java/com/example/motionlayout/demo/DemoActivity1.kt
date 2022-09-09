package com.example.motionlayout.demo

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlayout.R
import com.example.motionlayout.binding
import com.example.motionlayout.databinding.ActivityDemolBinding

class DemoActivity1 : AppCompatActivity() {

    private val binding: ActivityDemolBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window?.apply {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, context.resources.displayMetrics.heightPixels)
            setGravity(Gravity.BOTTOM)
        }
        binding.apply {
            mainPageView.setOnClickListener {
                secondFloorDragAreaView.setContent("第二层", null)
                motionLayout.setTransition(R.id.firstFloorTransition)
                motionLayout.transitionToEnd()
            }
            secondFloorContainer.setOnClickListener {
                thirdFloorDragAreaView.setContent("第三层", null)
                motionLayout.setTransition(R.id.secondFloorTransition)
                motionLayout.transitionToEnd()
            }
            secondFloorDragAreaView.setCloseListener {
                if (motionLayout.currentState == R.id.secondFloor) {
                    motionLayout.setTransition(R.id.firstFloorTransition)
                    motionLayout.transitionToStart()
                }
            }
            thirdFloorDragAreaView.setCloseListener {
                if (motionLayout.currentState == R.id.thirdFloor) {
                    motionLayout.setTransition(R.id.secondFloorTransition)
                    motionLayout.transitionToStart()
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        slideInOut()
    }

    override fun finish() {
        super.finish()
        slideInOut()
    }

    private fun Activity.slideInOut() {
        overridePendingTransition(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
    }
}
