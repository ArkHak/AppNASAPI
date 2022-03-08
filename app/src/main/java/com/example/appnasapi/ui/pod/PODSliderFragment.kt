package com.example.appnasapi.ui.pod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.appnasapi.R
import com.example.appnasapi.ui.pod.SliderPodAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_main_slider_pod.*


class PODSliderFragment : Fragment() {

    private val DEFAULT_POSITION = 2

    private lateinit var adapter: SliderPodAdapter
    private lateinit var viewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_slider_pod, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SliderPodAdapter(this)
        viewPager = pager

        viewPager.adapter = adapter
        viewPager.setPageTransformer(ZoomOutPageTransformer())

        viewPager.setCurrentItem(DEFAULT_POSITION, true)

        TabLayoutMediator(tab_layout_pod, pager) { tab, position ->
            tab.text = tabNames(position)
        }.attach()

    }

    private fun tabNames(position: Int): String? {
        when (position) {
            0 -> return getString(R.string.day_before_yesterday)
            1 -> return getString(R.string.yesterday)
            2 -> return getString(R.string.today)
        }
        return null
    }

    class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        private val MIN_SCALE = 0.85f
        private val MIN_ALPHA = 0.5f

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> {
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        alpha = 0f
                    }
                }
            }
        }

    }
}