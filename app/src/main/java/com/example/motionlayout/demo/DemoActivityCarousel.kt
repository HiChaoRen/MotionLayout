package com.example.motionlayout.demo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import com.example.motionlayout.R
import com.example.motionlayout.binding
import com.example.motionlayout.databinding.ActivityDemoCarouselBinding
import com.google.android.material.card.MaterialCardView

/**
 * Created by HiChaoRen on 2022-9-9.
 */
class DemoActivityCarousel : AppCompatActivity() {

    private val binding: ActivityDemoCarouselBinding by binding()

    private val cardList = ArrayList<Pair<String, Int>>().apply {
        add("**** 1234" to R.drawable.citi)
        add("**** 1111" to R.drawable.bg_copy_demo)
        add("**** 4567" to R.drawable.standard_chartered)
        add("**** 2222" to R.drawable.bg_export_demo)
        add("**** 7890" to R.drawable.mastercard)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return cardList.size
            }

            override fun populate(view: View, index: Int) {
                val cardView = view as MaterialCardView
                cardView.findViewById<ImageView>(R.id.ivCreditCard).setImageResource(cardList[index].second)
                cardView.findViewById<TextView>(R.id.cardNumber).text = cardList[index].first
            }

            override fun onNewItem(index: Int) {
            }
        })
    }
}