package com.example.motionlayout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

/**
 * Created by HiChaoRen on 2021/8/19.
 */
inline fun <reified VB : ViewBinding> ComponentActivity.binding() = lazy {
    inflateBinding<VB>(layoutInflater).also {
        setContentView(it.root)
        if (this is ViewDataBinding) lifecycleOwner = this@binding
    }
}

inline fun <reified VB : ViewBinding> ViewGroup.binding(attachToParent: Boolean = true) = lazy {
    inflateBinding<VB>(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}

inline fun <reified VB : ViewBinding> inflateBinding(
    layoutInflater: LayoutInflater
) = VB::class.java.getMethod(
    "inflate",
    LayoutInflater::class.java
).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(
    parent: ViewGroup
) = inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false)

inline fun <reified VB : ViewBinding> inflateBinding(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) = VB::class.java.getMethod(
    "inflate",
    LayoutInflater::class.java,
    ViewGroup::class.java,
    Boolean::class.java
).invoke(null, layoutInflater, parent, attachToParent) as VB