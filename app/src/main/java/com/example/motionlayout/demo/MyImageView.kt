package com.example.motionlayout.demo

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.motionlayout.R

/**
 * Created by HiChaoRen on 2022-9-7.
 */
class MyImageView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle) {

    fun changeImage() {
        setImageResource(R.drawable.el_icon)
    }

    fun revertImage() {
        setImageResource(R.drawable.tc_icon)
    }
}