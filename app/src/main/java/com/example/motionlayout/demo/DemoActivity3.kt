package com.example.motionlayout.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.motionlayout.R
import com.example.motionlayout.StickDecoration
import com.example.motionlayout.binding
import com.example.motionlayout.databinding.ActivityDemo3Binding
import com.example.motionlayout.databinding.AssistantFlightContentViewBinding

/**
 * Created by HiChaoRen on 2022-10-31.
 */
class DemoActivity3 : AppCompatActivity() {

    private val binding: ActivityDemo3Binding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.recyclerView.adapter = Adapter()
        binding.recyclerView.addItemDecoration(object : StickDecoration() {
            override fun onCreateStickView(positionInRecycler: Int, parent: RecyclerView): View? {
                if (positionInRecycler >= 0) {
                    val mStickView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_stick_view, parent, false)
                    var str = "苏州之旅$positionInRecycler"
                    mStickView?.findViewById<TextView>(R.id.dptStartTime)?.text = str
                    return mStickView
                }
                return super.onCreateStickView(positionInRecycler, parent)
            }
        })
    }



    private class Adapter :RecyclerView.Adapter<RecyclerViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val itemView = DataBindingUtil.inflate<AssistantFlightContentViewBinding>(LayoutInflater.from(parent.context), R.layout.assistant_flight_content_view,parent,false)
            return RecyclerViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        }

        override fun getItemCount(): Int {
            return 100
        }

    }
}