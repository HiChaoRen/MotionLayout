@file:Suppress("unused")
package com.example.motionlayout.ktx

import androidx.constraintlayout.motion.widget.MotionLayout

/**
 * Created by HiChaoRen on 2022-9-14.
 */

internal inline fun MotionLayout.doOnTransitionStarted(
    crossinline action: (
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int
    ) -> Unit
) = addTransitionListener(onTransitionStarted = action)

internal inline fun MotionLayout.doOnTransitionChange(
    crossinline action: (
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) -> Unit
) = addTransitionListener(onTransitionChange = action)

internal inline fun MotionLayout.doOnTransitionCompleted(
    crossinline action: (
        motionLayout: MotionLayout?,
        currentId: Int
    ) -> Unit
) = addTransitionListener(onTransitionCompleted = action)

internal inline fun MotionLayout.doOnTransitionTrigger(
    crossinline action: (
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) -> Unit
) = addTransitionListener(onTransitionTrigger = action)

internal inline fun MotionLayout.addTransitionListener(
    crossinline onTransitionStarted: (
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int
    ) -> Unit = { _, _, _ -> },
    crossinline onTransitionChange: (
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTransitionCompleted: (
        motionLayout: MotionLayout?,
        currentId: Int
    ) -> Unit = { _, _ -> },
    crossinline onTransitionTrigger: (
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) -> Unit = { _, _, _, _ -> }

): MotionLayout.TransitionListener {
    val listener = object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            onTransitionStarted.invoke(p0, p1, p2)
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            onTransitionChange.invoke(p0, p1, p2, p3)
        }

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            onTransitionCompleted.invoke(p0, p1)
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            onTransitionTrigger(p0, p1, p2, p3)
        }
    }
    addTransitionListener(listener)
    return listener
}
