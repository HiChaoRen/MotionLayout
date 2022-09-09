package com.example.motionlayout.demo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.motionlayout.R

/**
 * Created by HiChaoRen on 2021/3/2.
 */
class HomeSearchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context).inflate(R.layout.home_search_view, this, true)
    }
}