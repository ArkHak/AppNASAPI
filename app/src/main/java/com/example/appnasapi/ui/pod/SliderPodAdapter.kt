package com.example.appnasapi.ui.pod

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnasapi.ui.pod.ARG_DAY_SLIDER
import com.example.appnasapi.ui.pod.PODFragment

const val NOW = "NOW"
const val YESTERDAY = "YESTERDAY"
const val TWO_DAY_AGO = "TWO_DAY_AGO"
const val MAX_DAY = 3


class SliderPodAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = MAX_DAY

    override fun createFragment(position: Int): Fragment {
        val fragment = PODFragment()
        fragment.arguments = Bundle().apply {
            putString(
                ARG_DAY_SLIDER,
                when (position) {
                    0 -> TWO_DAY_AGO
                    1 -> YESTERDAY
                    2 -> NOW
                    else -> TWO_DAY_AGO
                }
            )
        }
        return fragment
    }

}
