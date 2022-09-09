package com.example.motionlayout.demo

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.motionlayout.R
import com.example.motionlayout.databinding.PayPopHeaderViewBinding

/**
 * Created by HiChaoRen on 2022-8-1.
 */
class BottomDragAreaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding: PayPopHeaderViewBinding by lazy {
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pay_pop_header_view, this, true)
    }

    fun setContent(title: CharSequence?, subTitle: CharSequence?) {
        binding.title.text = title
        binding.subTitle.text = subTitle
        binding.subTitle.isVisible = !(TextUtils.isEmpty(subTitle))
    }

    fun setCloseListener(listener: OnClickListener) {
        binding.closeIcon.setOnClickListener(listener)
    }

}